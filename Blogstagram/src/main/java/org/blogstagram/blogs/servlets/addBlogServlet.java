package org.blogstagram.blogs.servlets;

import com.google.gson.Gson;
import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.HashTag;
import org.blogstagram.models.User;
import org.blogstagram.validators.BlogValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.blogstagram.blogs.api.BlogStatusCodes;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@WebServlet("/addBlog")
public class addBlogServlet extends HttpServlet {
    private final static String BLOGPAGE = "/jsp/blog/blogPage.jsp";
    private HttpSession getSession(HttpServletRequest request){ return request.getSession(); }


    private List <User> getModerators(HttpServletRequest request, UserDAO userDAO){
        List <User> moderators = new ArrayList<>();
        String moderatorsList = request.getParameter("moderators");
        final JSONObject object = new JSONObject(moderatorsList);
        final JSONArray moderatorArray = object.getJSONArray("users");
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


    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getRequestDispatcher(BLOGPAGE).forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // session
        HttpSession session = getSession(request);

        //context
        ServletContext context = request.getServletContext();

        // dao objects
        UserDAO userDAO = (UserDAO) session.getAttribute("UserDAO");
        SqlBlogDAO blogDAO = (SqlBlogDAO) session.getAttribute("blogDao");
        //logged in user
        Integer currentUserId = (Integer) session.getAttribute("currentUserID");

        //connection
        Connection conenction = (Connection) context.getAttribute("dbConnection");
        // response json
        JSONObject responseJson = new JSONObject();
        if(currentUserId != null) {
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            List <User> moderators = getModerators(request, userDAO);
            List <HashTag> hashTags = getHashTags(request);
            Blog newBlog = new Blog();
            newBlog.setUser_id(currentUserId);
            newBlog.setTitle(title);
            newBlog.setContent(content);
            newBlog.setBlogModerators(moderators);
            newBlog.setHashTagList(hashTags);
            BlogValidator validator = new BlogValidator(newBlog, blogDAO);
            try {
                validator.validate();
                List<GeneralError> errors = validator.getErrors();
                if (errors.size() != 0) {
                    Gson gson = new Gson();
                    responseJson.put("status", BlogStatusCodes.error);
                    responseJson.put("errors", gson.toJson(errors));
                } else {
                    blogDAO.addBlog(newBlog);
                    responseJson.put("blogId", newBlog.getId());
                    responseJson.put("status", BlogStatusCodes.ADDED);
                }
            } catch (InvalidSQLQueryException | DatabaseError | SQLException e) {
                responseJson.put("status", BlogStatusCodes.error);
            } finally {
                response.getWriter().print(responseJson);
            }
        }  else {
            response.sendError(response.SC_NOT_FOUND);
        }

    }

    private List<HashTag> getHashTags(HttpServletRequest request) {
        List <HashTag> hashTags = new ArrayList<>();
        String hashTagsStr = request.getParameter("hashtags");
        JSONObject hashTagsObj = new JSONObject(hashTagsStr);
        JSONArray array = hashTagsObj.getJSONArray("hashTags");
        for(int k = 0; k < array.length(); k++){
            JSONObject currentHashTag = array.getJSONObject(k);
            HashTag newHashTag = new HashTag(currentHashTag.getString("hashTag"));
            hashTags.add(newHashTag);
        }
        return hashTags;
    }
}
