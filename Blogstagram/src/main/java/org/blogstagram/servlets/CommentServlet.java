package org.blogstagram.servlets;

import com.google.gson.Gson;
import org.blogstagram.Validators.CommentValidator;
import org.blogstagram.dao.CommentDAO;
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

        String requestType = request.getParameter("CommentAction");
        String user_id = "11";//(String) request.getAttribute("currentUserID");
        String blog_id = request.getParameter("blog_id");
        // if user wants to add comment, string keeps comment text
        // if he wants to delete comment it keeps comment id
        String comment = request.getParameter("Comment");
        int comment_id;
        if (!request.getParameter("comment_id").equals("")){
            comment_id = Integer.parseInt(request.getParameter("comment_id"));
        }else{
            comment_id = -1;
        }
        CommentDAO commentDAO = (CommentDAO)context.getAttribute("CommentDAO");
        CommentValidator commValidator = new CommentValidator();
        commValidator.setCommentDAO(commentDAO);

        List<VariableError> errorList = new ArrayList<>();
        try {
            if(requestType.equals("AddComment") && commValidator.validate(comment)){
                Comment newComment = new Comment(Integer.parseInt(user_id), Integer.parseInt(blog_id),
                        comment, new Date(System.currentTimeMillis()));
                commentDAO.addComment(newComment);
            }else if(requestType.equals("DeleteComment") && commValidator.commentDeleteValidator(comment_id)) {
                commentDAO.deleteComment(comment_id);
            }else if(requestType.equals("EditComment") && commValidator.commentDeleteValidator(comment_id)){
                commentDAO.editComment(comment_id, comment);
            }else{
                VariableError varError = new VariableError("Comment System", "comment not valid");
                errorList.add(varError);

                Gson gson = new Gson();
                response.getWriter().print(gson.toJson(errorList));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){

    }
}
