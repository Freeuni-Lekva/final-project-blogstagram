package org.blogstagram.servlets;

import com.google.gson.Gson;
import org.blogstagram.Validators.*;
import org.blogstagram.dao.CommentDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.Comment;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/commentServlet")
public class CommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  throws IOException {
        ServletContext context = request.getServletContext();
        UserDAO userDao = (UserDAO)request.getSession().getAttribute("UserDAO");
        String requestType = request.getParameter("CommentAction");
        String user_id = "11";//(String) request.getSession().getAttribute("currentUserID");
        String blog_id = request.getParameter("blog_id");
        // if user wants to add comment, string keeps comment text
        // if he wants to delete comment it keeps comment id
        String comment = request.getParameter("Comment");
        int comment_id;
        if (!request.getParameter("comment_id").equals("")){
            comment_id = Integer.parseInt(request.getParameter("comment_id"));
        }else{
            comment_id = Comment.NO_ID;
        }
        CommentDAO commentDAO = (CommentDAO)context.getAttribute("CommentDAO");
        BlogExistsValidator blogValidator = new BlogExistsValidator();
        blogValidator.setCommentDAO(commentDAO);
        UserIdValidator userVal = new UserIdValidator();
        userVal.setUserDao(userDao);

        List<VariableError> errorList = new ArrayList<>();
        try {
            if(userVal.validate(user_id) && requestType.equals("AddComment") && blogValidator.validate(blog_id)){
                CommentAddValidator commAddValidator = new CommentAddValidator();
                commAddValidator.setCommentDAO(commentDAO);
                if(commAddValidator.validate(comment)) {
                    Comment newComment = new Comment(Integer.parseInt(user_id), Integer.parseInt(blog_id),
                            comment, new Date(System.currentTimeMillis()));
                    commentDAO.addComment(newComment);
                }
            }else if(userVal.validate(user_id) && requestType.equals("DeleteComment") && blogValidator.validate(blog_id)) {
                CommentDeleteValidator commDeleteValidator = new CommentDeleteValidator();
                commDeleteValidator.setCommentDAO(commentDAO);
                if(commDeleteValidator.validate(comment_id)) {
                    commentDAO.deleteComment(comment_id);
                }
            }else if(userVal.validate(user_id) && requestType.equals("EditComment") && blogValidator.validate(blog_id)){
                CommentExistsValidator commExistsValidator = new CommentExistsValidator();
                commExistsValidator.setCommentDAO(commentDAO);
                if(commExistsValidator.validate(comment_id)){
                    commentDAO.editComment(comment_id, comment);
                }
            }else{
                VariableError varError = new VariableError("Comment System", "comment not valid");
                errorList.add(varError);

                Gson gson = new Gson();
                response.getWriter().print(gson.toJson(errorList));
            }
        } catch (SQLException | DatabaseError | NotValidUserIdException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){

    }
}
