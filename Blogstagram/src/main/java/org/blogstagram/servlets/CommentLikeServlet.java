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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

        CommentLikeValidator val = new CommentLikeValidator();
        val.setConnection(connection);
        UserIdValidator userVal = new UserIdValidator();
        userVal.setUserDao(userDao);

        String comment_id = request.getParameter("comment_id");
        List<GeneralError> errorList = new ArrayList<>();
        String requestType = request.getParameter("Like");
        try{
            // if comment is not liked and user wants to like
            if(userVal.validate(user_id) && !val.validate(comment_id, user_id) && requestType.equals("Like")){
                commentDAO.likeComment( Integer.parseInt(comment_id), user_id);
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