package org.blogstagram.blogLike.blogLikeServlets;

import com.google.gson.Gson;
import org.blogstagram.dao.BlogLikeDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.User;
import org.blogstagram.validators.BlogExistsValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/likes")
public class LikersListServlet extends HttpServlet {

    private Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection)req.getServletContext().getAttribute("dbConnection");
        return connection;
    }

    private BlogLikeDao getBlogLikeDaoFromSession(HttpServletRequest req){
        BlogLikeDao blogLikeDao = (BlogLikeDao) req.getSession().getAttribute("BlogLikeDao");
        return blogLikeDao;
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = getConnectionFromContext(request);
        BlogExistsValidator blogExistsValidator = new BlogExistsValidator();
        blogExistsValidator.setConnection(connection);
        BlogLikeDao blogLikeDao = getBlogLikeDaoFromSession(request);
        String blogId = request.getParameter("blog_id");
        ArrayList<GeneralError> errors = new ArrayList<>();
        try {
            if(blogExistsValidator.validate(blogId, null)) {
                ArrayList<User> likes = blogLikeDao.getBlogLikers(Integer.parseInt(blogId));
                request.getSession().setAttribute("likes", likes);
            } else {
                VariableError variableError = new VariableError("BlogLikes", "Blog id doesn't exist");
                errors.add(variableError);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Gson gson = new Gson();
        response.getWriter().print(gson.toJson(errors));
    }
}
