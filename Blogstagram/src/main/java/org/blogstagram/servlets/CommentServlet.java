package org.blogstagram.servlets;

import com.google.gson.Gson;
import org.blogstagram.dao.BlogDAO;
import org.blogstagram.dao.NotificationDao;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.Notification;
import org.blogstagram.validators.*;
import org.blogstagram.dao.CommentDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.Comment;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.blogstagram.notifications.NotificationConstants.NotificationTypes.POST_COMMENT_NOTIFICATION;
import static org.blogstagram.notifications.NotificationConstants.NotificationTypes.POST_LIKE_NOTIFICATION;
import static org.blogstagram.notifications.NotificationConstants.SeenStatus.NOT_SEEN;

@WebServlet("/commentServlet")
public class CommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  throws IOException {
        Integer user_id = (Integer) request.getSession().getAttribute("currentUserID");
        if(user_id == null){
            response.sendError(response.SC_UNAUTHORIZED);
            return;
        }
        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");
        UserDAO userDao = (UserDAO)request.getSession().getAttribute("UserDAO");
        BlogDAO blogDAO = (BlogDAO) request.getSession().getAttribute("blogDao");
        NotificationDao notificationDao = (NotificationDao) request.getSession().getAttribute("NotificationDao");
        String requestType = request.getParameter("CommentAction");
        String blog_id = request.getParameter("blog_id");
        String comment = request.getParameter("Comment");
        int comment_id;
        if (request.getParameter("comment_id") != null){
            comment_id = Integer.parseInt(request.getParameter("comment_id"));
        }else{
            comment_id = -1;
        }
        CommentDAO commentDAO = (CommentDAO)request.getSession().getAttribute("CommentDAO");
        BlogExistsValidator blogValidator = new BlogExistsValidator();
        blogValidator.setConnection(connection);
        UserIdValidator userVal = new UserIdValidator();
        userVal.setUserDao(userDao);

        List<VariableError> errorList = new ArrayList<>();
        Gson gson = new Gson();
        JSONObject responseJson = new JSONObject();
        try {
            if(userVal.validate(user_id) && requestType.equals("AddComment") && blogValidator.validate(blog_id,"")){
                CommentAddValidator commAddValidator = new CommentAddValidator();
                if(commAddValidator.validate(comment, "")) {
                    Comment newComment = new Comment(user_id, Integer.parseInt(blog_id),
                            comment, new Date(System.currentTimeMillis()));
                    commentDAO.addComment(newComment);
                    Blog blog = blogDAO.getBlog(Integer.parseInt(blog_id));
                    Notification notification = new Notification(null, POST_COMMENT_NOTIFICATION, user_id, blog.getUser_id(), blog.getId(), NOT_SEEN, new Date(System.currentTimeMillis()));
                    notificationDao.addNotification(notification);
                    responseJson.put("comment", gson.toJson(newComment));
                }
            }else if(userVal.validate(user_id) && requestType.equals("DeleteComment") && blogValidator.validate(blog_id,"")) {
                CommentDeleteValidator commDeleteValidator = new CommentDeleteValidator();
                commDeleteValidator.setConnection(connection);
                if(commDeleteValidator.validate(comment_id, user_id)) {
                    commentDAO.deleteComment(comment_id);
                }else{
                    VariableError varError = new VariableError("Comment System", "can not delete comment of different user");
                    errorList.add(varError);
                }
            }else if(userVal.validate(user_id) && requestType.equals("EditComment") && blogValidator.validate(blog_id,"")){
                CommentExistsValidator commExistsValidator = new CommentExistsValidator();
                commExistsValidator.setConnection(connection);
                if(commExistsValidator.validate(comment_id, user_id)){
                    commentDAO.editComment(comment_id, comment);
                }else{
                    VariableError varError = new VariableError("Comment System", "can not edit comment of different user");
                    errorList.add(varError);
                }
            }else{
                VariableError varError = new VariableError("Comment System", "comment action not valid");
                errorList.add(varError);
            }
        } catch (SQLException | DatabaseError | NotValidUserIdException | InvalidSQLQueryException throwables) {
            throwables.printStackTrace();
        }
        responseJson.put("errors", gson.toJson(errorList));
        response.getWriter().print(responseJson);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){

    }
}
