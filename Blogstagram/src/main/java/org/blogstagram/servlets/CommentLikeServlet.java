package org.blogstagram.servlets;

import com.google.gson.Gson;
import org.blogstagram.dao.BlogDAO;
import org.blogstagram.dao.NotificationDao;
import org.blogstagram.errors.*;
import org.blogstagram.models.Blog;
import org.blogstagram.models.Notification;
import org.blogstagram.validators.CommentLikeValidator;
import org.blogstagram.validators.UserIdValidator;
import org.blogstagram.dao.CommentDAO;
import org.blogstagram.dao.UserDAO;

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

import static org.blogstagram.notifications.NotificationConstants.NotificationTypes.COMMENT_LIKE_NOTIFICATION;
import static org.blogstagram.notifications.NotificationConstants.NotificationTypes.POST_LIKE_NOTIFICATION;
import static org.blogstagram.notifications.NotificationConstants.SeenStatus.NOT_SEEN;

@WebServlet("/commentLike")
public class CommentLikeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer user_id = (Integer) request.getSession().getAttribute("currentUserID");
        if(user_id == null){
            response.sendError(response.SC_UNAUTHORIZED);
            return;
        }
        Connection connection = (Connection) request.getServletContext().getAttribute("dbConnection");
        ServletContext context = request.getServletContext();
        CommentDAO commentDAO = (CommentDAO)request.getSession().getAttribute("CommentDAO");
        UserDAO userDao = (UserDAO)request.getSession().getAttribute("UserDAO");
//        BlogDAO blogDAO = (BlogDAO)request.getSession().getAttribute("blogDao");

        NotificationDao notificationDao = (NotificationDao)request.getSession().getAttribute("NotificationDao");

        CommentLikeValidator val = new CommentLikeValidator();
        val.setConnection(connection);
        UserIdValidator userVal = new UserIdValidator();
        userVal.setUserDao(userDao);

//        String blogId = request.getParameter("blog_id");


        String comment_id = request.getParameter("comment_id");
        List<GeneralError> errorList = new ArrayList<>();
        String requestType = request.getParameter("Like");
        try{
            // if comment is not liked and user wants to like
            if(userVal.validate(user_id) && !val.validate(comment_id, user_id) && requestType.equals("Like")){
//                Blog blog = blogDAO.getBlog(Integer.parseInt(blogId));
//                commentDAO.likeComment( Integer.parseInt(comment_id), user_id);
//                Notification notification = new Notification(null, COMMENT_LIKE_NOTIFICATION, user_id, blog.getUser_id(), blog.getId(), NOT_SEEN, new Date(System.currentTimeMillis()));
//                notificationDao.addNotification(notification);
                // if comment is liked and user wants to unlike
            }else if(userVal.validate(user_id) && val.validate(comment_id, user_id) && requestType.equals("Unlike")){
                commentDAO.unlikeComment(Integer.parseInt(comment_id), user_id);
            }else{
                // if comment is liked and user wants to like again
                if(val.validate(comment_id, user_id) && requestType.equals("Like")) {
                    VariableError varError = new VariableError("CommentLike", "Can not like already liked comment");
                    errorList.add(varError);
                }else if(!val.validate(comment_id, user_id) && requestType.equals("Unlike")){
                    VariableError varError = new VariableError("CommentLike", "Can not unlike non liked comment");
                    errorList.add(varError);
                }else if(!userVal.validate(user_id)){
                    VariableError varError = new VariableError("CommentLike", "User ID trying to like is not valid");
                    errorList.add(varError);
                }
            }
        } catch (SQLException | DatabaseError | NotValidUserIdException throwables) {
            throwables.printStackTrace();
        }

        Gson gson = new Gson();
        response.getWriter().print(gson.toJson(errorList));

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){

    }
}