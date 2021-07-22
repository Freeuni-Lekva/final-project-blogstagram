package org.blogstagram.servlets;

import com.google.gson.Gson;
import org.blogstagram.validators.CommentLikeValidator;
import org.blogstagram.validators.UserIdValidator;
import org.blogstagram.dao.CommentDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.errors.VariableError;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/commentLike")
public class CommentLikeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user_id = (String) request.getSession().getAttribute("currentUserID");
        if(user_id == null){
            response.sendError(response.SC_UNAUTHORIZED);
            return;
        }
        ServletContext context = request.getServletContext();
        CommentDAO commentDAO = (CommentDAO)context.getAttribute("CommentDAO");
        UserDAO userDao = (UserDAO)request.getSession().getAttribute("UserDAO");
        CommentLikeValidator val = new CommentLikeValidator();
        val.setCommentDAO(commentDAO);
        UserIdValidator userVal = new UserIdValidator();
        userVal.setUserDao(userDao);
        String comment_id = request.getParameter("comment_id");
        System.out.println(comment_id);
        List<GeneralError> errorList = new ArrayList<>();
        String requestType = request.getParameter("Like");
        try{
            // if comment is not liked and user wants to like
            if(userVal.validate(user_id) && !val.validate(comment_id, user_id) && requestType.equals("Like")){
                commentDAO.likeComment( Integer.parseInt(comment_id), Integer.parseInt(user_id));
            // if comment is liked and user wants to unlike
            }else if(userVal.validate(user_id) && val.validate(comment_id, user_id) && requestType.equals("Unlike")){
                commentDAO.unlikeComment(Integer.parseInt(comment_id), Integer.parseInt(user_id));
                System.out.println("Comment was unliked");
            }else{
                // if comment is liked and user wants to like again
                if(val.validate(comment_id, user_id) && requestType.equals("Like")){
                    VariableError varError = new VariableError("CommentLike", "Can not like already liked comment");
                    errorList.add(varError);
                    Gson gson = new Gson();
                    response.getWriter().print(gson.toJson(errorList));
                }else if(!userVal.validate(user_id)){
                    VariableError varError = new VariableError("CommentLike", "User ID trying to like is not valid");
                    errorList.add(varError);
                    Gson gson = new Gson();
                    response.getWriter().print(gson.toJson(errorList));
                }
            }
        } catch (SQLException | DatabaseError | NotValidUserIdException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){

    }
}
