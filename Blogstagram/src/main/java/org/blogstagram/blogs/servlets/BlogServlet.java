package org.blogstagram.blogs.servlets;

import com.google.gson.JsonObject;
import org.blogstagram.blogs.api.BlogStatusCodes;
import org.blogstagram.dao.BlogDAO;
import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.*;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.models.Blog;
import org.blogstagram.models.HashTag;
import org.blogstagram.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.blogstagram.pairs.StringPair;
import org.blogstagram.validators.*;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/blog/*")
public class BlogServlet extends HttpServlet {

    private final static String BLOGPAGE = "/jsp/blog/blogPage.jsp";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String MODERATORS = "moderators";
    private static final String EDIT = "edit";
    private static final String REMOVE = "remove";
    private static final String HASHTAGS = "hashtags";

    private HttpSession getSession(HttpServletRequest request){
        return request.getSession();
    }

    private FollowApi initFollowApi(UserDAO userDAO, SqlFollowDao followDao){
        FollowApi follow = new FollowApi();
        follow.setUserDao(userDAO);
        follow.setFollowDao(followDao);
        return follow;
    }

    private getBlogRequestValidator initValidator(Integer currentUserId, UserDAO userDAO, Blog currentBlog, FollowApi api){
        getBlogRequestValidator validator = new getBlogRequestValidator();
        validator.setFollowApi(api);
        validator.setUserDAO(userDAO);
        validator.setUserid(currentUserId);
        validator.setCurrentBlog(currentBlog);
        return validator;
    }

    private List<User> getModerators(JSONObject json, UserDAO userDAO){
        List <User> moderators = new ArrayList<>();
        final JSONArray moderatorArray = json.getJSONArray("moderators");
        for(int k = 0; k < moderatorArray.length(); k++){
            final JSONObject moderatorJson = moderatorArray.getJSONObject(k);
            try {
                User moderator = userDAO.getUserByNickname(moderatorJson.getString("nickname"));
                moderators.add(moderator);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return moderators;
    }


    private StringPair getPathIdentificator(HttpServletRequest req){
        String pathInfo = req.getPathInfo();
        if(pathInfo == null)
            return null;
        String[] pathParts = pathInfo.split("/");

        if(pathParts.length > 3)
            return null;
        String blogIdentificator = pathParts[1];
        String type = null;
        if(pathParts.length > 2){
            type = pathParts[2];
        }

        return new StringPair(blogIdentificator, type);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        

        StringPair pair = getPathIdentificator(request);
        if(pair == null || pair.getKey() == null) {
            response.sendError(response.SC_NOT_FOUND);
            return;
        }
        // session
        HttpSession session = getSession(request);

        //blog and current user id
        Integer currentUserId = (Integer) session.getAttribute("currentUserID");

        Integer blogId = Integer.parseInt(pair.getKey());

        // edited content
        String edited = request.getParameter("edited");

        // dao objects
        SqlBlogDAO blogDAO = (SqlBlogDAO) session.getAttribute("blogDao");
        UserDAO userDAO = (UserDAO) session.getAttribute("UserDAO");

        // operation type
        String operationType = pair.getValue();

        // response json
        JSONObject responseJson = new JSONObject();

        if(currentUserId == null){
            response.sendError(response.SC_NOT_FOUND);
            return;
        }

        try {
            Validator blogIdValidator = new BlogIdValidator(blogId, blogDAO);
            blogIdValidator.validate();
            List <GeneralError> errors = blogIdValidator.getErrors();
            if(errors.size() != 0){
                responseJson.append("status", BlogStatusCodes.error);
                response.getWriter().print(errors);
                response.getWriter().print(responseJson);
                return;
            }

            Validator validator = null;
            Blog current = blogDAO.getBlog(blogId);
            if(operationType.equals(EDIT)) {
                validator = new EditRequestValidator(currentUserId, blogId, blogDAO);
                validator.validate();
                List <GeneralError> errs = validator.getErrors();
                if(errs.size() != 0){
                    response.getWriter().print(errs);
                    return;
                }
                Blog editedBlog = getEdittedContent(edited, current, request, userDAO);
                Validator blogValidator = new BlogValidator(editedBlog, blogDAO);
                blogValidator.validate();
                List <GeneralError> blogErrs = blogValidator.getErrors();
                if(blogErrs.size() != 0){
                    response.getWriter().print(blogErrs);
                    return;
                }
                blogDAO.editBlog(editedBlog);
                responseJson.append("status", BlogStatusCodes.EDITED);
            } else if(operationType.equals(REMOVE)) {
                validator = new RemoveRequestValidator(blogId, currentUserId, blogDAO, userDAO);
                validator.validate();
                List <GeneralError> errs = validator.getErrors();
                if(errs.size() != 0){
                    response.getWriter().print(errs);
                    return;
                }
                blogDAO.removeBlog(current);
                responseJson.append("status", BlogStatusCodes.REMOVED);
            } else {
                responseJson.append("status", BlogStatusCodes.error);
                response.sendError(response.SC_NOT_FOUND);
            }
        } catch (InvalidSQLQueryException | DatabaseError | SQLException e) {
            responseJson.append("message", e);
            responseJson.append("status", BlogStatusCodes.error);
        } finally {
            response.getWriter().print(responseJson);
        }
    }

    private Blog getEdittedContent(String editedJson, Blog current, HttpServletRequest request, UserDAO userDAO) {
        JSONObject json =  new JSONObject(editedJson);
        Iterator <String> keys = json.keys();
        Blog edited = new Blog(current);
        while(keys.hasNext()){
            editField(edited, keys.next(), json, request, userDAO);
        }
        return edited;
    }

    private void editField(Blog edited, String field, JSONObject json, HttpServletRequest request, UserDAO userDAO) {
        switch (field) {
            case TITLE:
                edited.setTitle(json.getString(field));
                break;
            case CONTENT:
                edited.setContent(json.getString(field));
                break;
            case MODERATORS:
                edited.setBlogModerators(getModerators(json, userDAO));
                break;
            case HASHTAGS:
                edited.setHashTagList(getHashTags(json, edited.getId()));
                break;
            default:
                throw new UnsupportedOperationException("field " + field + " is not Blog field");
        }
    }

    private List<HashTag> getHashTags(JSONObject json, int blogId) {
        List <HashTag> hashTagList = new ArrayList<>();
        JSONArray hashTagArr = json.getJSONArray("hashtags");
        for(int k = 0; k < hashTagArr.length(); k++){
            HashTag newHashTag = new HashTag(hashTagArr.getJSONObject(k).getString("hashtag"));
            hashTagList.add(newHashTag);
        }
        return hashTagList;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           request.getSession().setAttribute("currentUserId", 1);
           StringPair urlPair = getPathIdentificator(request);
           if(urlPair == null || urlPair.getKey() == null || urlPair.getValue() != null){
               response.sendError(response.SC_NOT_FOUND);
               return;
           }

           //session
           HttpSession session = getSession(request);

           // user and blog id's
           Integer currentUserId = (Integer) session.getAttribute("currentUserId");
           int blogId = Integer.parseInt(urlPair.getKey());

           // dao objects and followApi
           UserDAO userDAO = (UserDAO) session.getAttribute("UserDAO");
           SqlFollowDao followDao = (SqlFollowDao) session.getAttribute("SqlFollowDao");
           SqlBlogDAO blogDAO = (SqlBlogDAO) session.getAttribute("blogDao");

           FollowApi followApi = initFollowApi(userDAO, followDao);

           // response json
           JSONObject responseJson = new JSONObject();

        try {
            Validator blogIdValidator = new BlogIdValidator(blogId, blogDAO);
            blogIdValidator.validate();
            List <GeneralError> errors = blogIdValidator.getErrors();
            if(errors.size() != 0){
                responseJson.append("status", BlogStatusCodes.error);
                response.getWriter().print(errors);
                response.sendError(response.SC_NOT_FOUND);
                return;
            }

            Blog currentBlog = blogDAO.getBlog(blogId);
            getBlogRequestValidator validator = initValidator(currentUserId, userDAO, currentBlog, followApi);
            if(validator.shouldBeShown()){
                responseJson.append("status", BlogStatusCodes.SHOW);
                request.setAttribute("blog", currentBlog);
                request.getRequestDispatcher(BLOGPAGE).forward(request, response);
            } else {
                responseJson.append("status", BlogStatusCodes.NOTSHOW);
                response.getWriter().print(responseJson);
            }
        } catch (InvalidSQLQueryException | DatabaseError | SQLException e) {
            responseJson.append("status", BlogStatusCodes.error);

        } finally {
            response.getWriter().print(responseJson);
        }

    }
}
