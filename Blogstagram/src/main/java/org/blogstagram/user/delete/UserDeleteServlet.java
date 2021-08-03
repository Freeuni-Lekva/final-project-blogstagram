package org.blogstagram.user.delete;

import com.google.gson.Gson;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.models.User;
import org.blogstagram.validators.DeleteUserValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/delete/user")
public class UserDeleteServlet extends HttpServlet {
    private boolean isUserLoggedIn(HttpServletRequest req) {
        Integer userID = (Integer) req.getSession().getAttribute("currentUserID");
        String nickname = (String) req.getSession().getAttribute("currentUserNickname");
        if(userID == null || nickname == null)
            return false;
        return true;
    }
    private UserDAO getUserDaoFromSession(HttpServletRequest req){
        UserDAO userDAO = (UserDAO) req.getSession().getAttribute("UserDAO");
        return userDAO;
    }
    private Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection) req.getServletContext().getAttribute("dbConnection");
        return connection;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        /* Do nothing if user is not logged in */
        if(!isUserLoggedIn(req)){
            res.sendError(res.SC_UNAUTHORIZED);
            return;
        }
        Integer currentUserID = (Integer)req.getSession().getAttribute("currentUserID");
        String strDeleteUserID = req.getParameter("deleteUserID");

        if(strDeleteUserID == null || strDeleteUserID.length() == 0){
            res.sendError(res.SC_BAD_REQUEST);
            return;
        }
        Integer deleteUserID = Integer.parseInt(strDeleteUserID);

        UserDAO userDAO = getUserDaoFromSession(req);
        DeleteUserValidator deleteUserValidator = new DeleteUserValidator(currentUserID,deleteUserID,userDAO);
        try {
            if(!deleteUserValidator.validate()){
                List<GeneralError> errors = deleteUserValidator.getErrors();
                Gson gson = new Gson();
                res.getWriter().print(gson.toJson(errors));
                return;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        try {
            userDAO.deleteUserByID(deleteUserID);
            res.getWriter().print((new Gson()).toJson("{success: true}"));

            if(currentUserID == deleteUserID){
                req.getSession().setAttribute("currentUserID",null);
                req.getSession().setAttribute("currentUserNickname",null);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
