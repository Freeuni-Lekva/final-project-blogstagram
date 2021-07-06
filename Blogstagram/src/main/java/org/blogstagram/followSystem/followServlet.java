package org.blogstagram.followSystem;

import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.errors.NotLoggedInException;
import org.blogstagram.errors.UserNotRegisteredException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import org.blogstagram.dao.userDao;


/*
    check if user is logged in check to user if account exists in data base, we have userName and id
 */



@WebServlet("/followSystem")
public class followServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");
        UserDao userDao = (UserDao) context.getAttribute("UserDAO");
        SqlFollowDao followSystem = (SqlFollowDao) context.getAttribute("SqlFollowDao");
        String toIdStr = request.getParameter("from_Id");
        String fromIdStr = (String) request.getSession().getAttribute("to_Id");
        try{
            Integer fromId = Integer.parseInt(fromIdStr);
            Integer toId = Integer.parseInt(toIdStr);
            if(isRequestValid(fromId, toId, connection, userDao)){
                if(followSystem.isAlreadyFollowed(fromId, toId)){
                    followSystem.unfollow(fromId, toId);
                }else{
                    followSystem.sendFollowRequest(fromId, toId);
                }
            }
        }catch (NotLoggedInException ex){
            // send json
        }catch (UserNotRegisteredException ex){
            // send json
        }catch (NumberFormatException ex){
            // send json
        }
    }

    private boolean isRequestValid(Integer fromId, Integer toId, Connection connection, UserDao usedDao) throws NotLoggedInException, UserNotRegisteredException {
        if(!userLoggedIn(fromId)) throw new NotLoggedInException("User must be logged in to follow/unfollow other user.");
        if(!isUserRegistered(userDAO)) throw new UserNotRegisteredException("Can't follow user who is not registered.");
        return true;
    }

    private boolean isUserRegistered(UserDao userDao, Integer toId) {
            return userDAO.getUserByIdOrNickname(toId, null) != null;
    }

    private boolean userLoggedIn(Integer fromId) {
        if(fromId == null)
            return false;
        return true;
    }
}
