package org.blogstagram.postLike.postLikeServlet;

import com.google.gson.Gson;
import org.blogstagram.dao.PostLikeDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.errors.VariableError;
import org.blogstagram.validators.PostLikeValidator;
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

@WebServlet("/postLike")
public class PostLikeServlet extends HttpServlet {

    private Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection)req.getServletContext().getAttribute("dbConnection");
        return connection;
    }

    private PostLikeDao getPostLikeDaoFromSession(HttpServletRequest req){
        PostLikeDao postLikeDao = (PostLikeDao) req.getSession().getAttribute("PostLikeDao");
        return postLikeDao;
    }

    private UserDAO getUserDaoFromSession(HttpServletRequest req){
        UserDAO userDAO = (UserDAO) req.getSession().getAttribute("UserDAO");
        return userDAO;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = (String) request.getSession().getAttribute("currentUserID");
        if(userId == null) {
            response.sendError(response.SC_UNAUTHORIZED);
            return;
        }
        Connection connection = getConnectionFromContext(request);
        PostLikeDao postLikeDao = getPostLikeDaoFromSession(request);
        UserDAO userDAO = getUserDaoFromSession(request);
        PostLikeValidator postLikeValidator = new PostLikeValidator();
        postLikeValidator.setConnection(connection);
        UserIdValidator userIdValidator = new UserIdValidator();
        userIdValidator.setUserDao(userDAO);
        String postId = request.getParameter("post_id");
        ArrayList<GeneralError> errors = new ArrayList<>();
        String requestType = request.getParameter("Like");

        try{
            if(userIdValidator.validate(userId) && !postLikeValidator.validate(postId, userId) && requestType.equals("Like")){
                postLikeDao.likePost( Integer.parseInt(postId), Integer.parseInt(userId));
            } else if(userIdValidator.validate(userId) && postLikeValidator.validate(postId, userId) && requestType.equals("Unlike")){
                postLikeDao.unlikePost(Integer.parseInt(postId), Integer.parseInt(userId));
            } else {
                if(postLikeValidator.validate(postId, userId) && requestType.equals("Like")) {
                    VariableError varError = new VariableError("PostLike", "Can not like already liked post");
                    errors.add(varError);
                } else if(!postLikeValidator.validate(postId, userId) && requestType.equals("Unlike")){
                    VariableError varError = new VariableError("PostLike", "Can not unlike non liked post");
                    errors.add(varError);
                } else if(!userIdValidator.validate(userId)){
                    VariableError varError = new VariableError("PostLike", "User ID trying to like is not valid");
                    errors.add(varError);
                }
            }
        } catch (SQLException | DatabaseError | NotValidUserIdException throwables) {
            throwables.printStackTrace();
        }
        if(errors.size() != 0) {
            Gson gson = new Gson();
            response.getWriter().print(gson.toJson(errors));
        }
    }
}
