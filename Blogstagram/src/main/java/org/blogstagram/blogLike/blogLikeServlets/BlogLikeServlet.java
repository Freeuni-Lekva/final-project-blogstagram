package org.blogstagram.blogLike.blogLikeServlets;

import com.google.gson.Gson;
import org.blogstagram.dao.BlogLikeDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.errors.VariableError;
import org.blogstagram.validators.BlogLikeValidator;
import org.blogstagram.validators.UserIdValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/blogLike")
public class BlogLikeServlet extends HttpServlet {

    private Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection)req.getServletContext().getAttribute("dbConnection");
        return connection;
    }

    private BlogLikeDao getBlogLikeDaoFromSession(HttpServletRequest req){
        BlogLikeDao blogLikeDao = (BlogLikeDao) req.getSession().getAttribute("BlogLikeDao");
        return blogLikeDao;
    }

    private UserDAO getUserDaoFromSession(HttpServletRequest req){
        UserDAO userDAO = (UserDAO) req.getSession().getAttribute("UserDAO");
        return userDAO;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("currentUserID");
        if(userId == null) {
            response.sendError(response.SC_UNAUTHORIZED);
            return;
        }
        Connection connection = getConnectionFromContext(request);
        BlogLikeDao blogLikeDao = getBlogLikeDaoFromSession(request);
        UserDAO userDAO = getUserDaoFromSession(request);
        BlogLikeValidator blogLikeValidator = new BlogLikeValidator();
        blogLikeValidator.setConnection(connection);
        UserIdValidator userIdValidator = new UserIdValidator();
        userIdValidator.setUserDao(userDAO);
        String blogId = request.getParameter("blog_id");
        ArrayList<GeneralError> errors = new ArrayList<>();
        String requestType = request.getParameter("Like");

        try{
            if(userIdValidator.validate(userId) && !blogLikeValidator.validate(blogId, userId) && requestType.equals("Like")){
                blogLikeDao.likeBlog( Integer.parseInt(blogId), userId);
            } else if(userIdValidator.validate(userId) && blogLikeValidator.validate(blogId, userId) && requestType.equals("Unlike")){
                blogLikeDao.unlikeBlog(Integer.parseInt(blogId), userId);
            } else {
                if(blogLikeValidator.validate(blogId, userId) && requestType.equals("Like")) {
                    VariableError varError = new VariableError("BlogLike", "Can not like already liked blog");
                    errors.add(varError);
                } else if(!blogLikeValidator.validate(blogId, userId) && requestType.equals("Unlike")){
                    VariableError varError = new VariableError("BlogLike", "Can not unlike non liked blog");
                    errors.add(varError);
                } else if(!userIdValidator.validate(userId)){
                    VariableError varError = new VariableError("BlogLike", "User ID trying to like is not valid");
                    errors.add(varError);
                }
            }
        } catch (SQLException | DatabaseError | NotValidUserIdException throwables) {
            throwables.printStackTrace();
        }
        Gson gson = new Gson();
        response.getWriter().print(gson.toJson(errors));
    }
}
