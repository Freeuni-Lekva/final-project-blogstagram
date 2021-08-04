package org.blogstagram.admin;

import com.google.gson.Gson;
import org.blogstagram.dao.AdminDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.errors.VariableError;
import org.blogstagram.validators.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminServlet extends HttpServlet{
    private AdminDAO adminDAO;
    private static final String DELETE_USER = "DeleteUser";
    private static final String DELETE_BLOG = "DeleteBlog";
    private static final String DELETE_COMMENT = "DeleteComment";

    private boolean isCorrect(String request){
        return request.equals(DELETE_USER) || request.equals(DELETE_COMMENT) || request.equals(DELETE_BLOG);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_id = (String) request.getSession().getAttribute("currentUserID");
        if(user_id == null){
            response.sendError(response.SC_UNAUTHORIZED);
            return;
        }
        Connection connection = (Connection) request.getServletContext().getAttribute("dbConnection");
        if(adminDAO == null){
            adminDAO = new AdminDAO(connection);
        }
        List<VariableError> errorList = new ArrayList<>();
        String requestType = request.getParameter("OperationType");
        if(isCorrect(requestType)){
            Gson gson = new Gson();
            response.getWriter().print(gson.toJson("{ request: 'request incorrect' }"));
            return;
        }
        AdminValidator adminValidator = new AdminValidator();
        adminValidator.setAdminDAOUser(adminDAO, Integer.parseInt(user_id));
        // validate that current user id is admin or moderator
        try {
            if(!adminValidator.validate()){
                Gson gson = new Gson();
                response.getWriter().print(gson.toJson("{User is not eligible to make these changes}"));
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            if (requestType.equals(DELETE_BLOG)) {
                String blog_id = request.getParameter("blog_id");
                BlogExistsValidator blogValidator = new BlogExistsValidator();
                blogValidator.setConnection(connection);

                if(blogValidator.validate(blog_id, "")) {
                    adminDAO.deleteBlog(Integer.parseInt(blog_id));
                }else{
                    VariableError error = new VariableError("Admin System", "Blog that was requested to be deleted does not exist");
                    errorList.add(error);
                }

            } else if (requestType.equals(DELETE_COMMENT)) {
                int comment_id = Integer.parseInt(request.getParameter("comment_id"));
                CommentExistsValidator commentValidator = new CommentExistsValidator();
                commentValidator.setConnection(connection);
                AdminCommentValidator adminCommentValidator = new AdminCommentValidator();
                adminCommentValidator.setConnectionComment(connection, comment_id);
                if(adminCommentValidator.validate()) {
                    adminDAO.deleteComment(comment_id);
                }else{
                    VariableError error = new VariableError("Admin System", "Comment that was requested to be deleted does not exist");
                    errorList.add(error);
                }

            } else if (requestType.equals(DELETE_USER)) {
                int to_delete_user_id = Integer.parseInt(request.getParameter("user_id"));
                UserDAO userDao = (UserDAO)request.getSession().getAttribute("UserDAO");
                UserIdValidator userIdValidator = new UserIdValidator();
                userIdValidator.setUserDao(userDao);
                if(userIdValidator.validate(to_delete_user_id)) {
                    adminDAO.deleteUser(to_delete_user_id);
                }else{
                    VariableError error = new VariableError("Admin System", "User that was requested to be deleted does not exist");
                    errorList.add(error);
                }
            }
        }catch (SQLException | DatabaseError | NotValidUserIdException throwables) {
            throwables.printStackTrace();
        }
        if(errorList.size() != 0) {
            Gson gson = new Gson();
            response.getWriter().print(gson.toJson(errorList));
        }


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }
}
