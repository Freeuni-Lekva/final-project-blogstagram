package org.blogstagram.user;

import org.blogstagram.dao.UserDAO;
import org.blogstagram.models.User;
import org.blogstagram.pairs.StringPair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    public static final String USER_PAGE_PATH = "/jsp/user/userPage.jsp";
    public static final String FOLLOWING_URL_IDENTIFICATOR = "following";
    public static final String FOLLOWERS_URL_IDENTIFICATOR = "followers";

    /*
     * URL : /user/identificator
     */
    private StringPair getPathIdentificator(HttpServletRequest req){
        String pathInfo = req.getPathInfo();
        if(pathInfo == null)
            return null;
        String[] pathParts = pathInfo.split("/");

        String userIdentificator = pathParts[1];
        String followIdentificator = null;
        if(pathParts.length > 2){
            followIdentificator = pathParts[2];
        }

        StringPair urlPair = new StringPair(userIdentificator,followIdentificator);


        return urlPair;
    }

    private UserDAO getUserDAO(HttpServletRequest req){
        UserDAO userDAO = (UserDAO) req.getSession().getAttribute("UserDAO");
        return userDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringPair urlPair = getPathIdentificator(req);
        if(urlPair == null){
            /*
             * Incorrect URL
             */
            res.sendError(res.SC_NOT_FOUND);
            return;
        }
        String userIdentificator = urlPair.getKey();
        String followIdentificator = urlPair.getValue();
        UserDAO userDAO = getUserDAO(req);

        if(followIdentificator == null){
            /*
             *  Logic for user page response
             */
            boolean isID = userIdentificator.matches("[0-9]+");
            User user = null;
            if(isID){
                Integer userID = Integer.parseInt(userIdentificator);
                try {
                    user = userDAO.getUserByID(userID);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } else {
                try {
                    user = userDAO.getUserByNickname(userIdentificator);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }

            /*
             *   Logic for unknown user
             */
            if(user == null){
                res.sendError(res.SC_NOT_FOUND);
                return;
            }
            /* ------------------------- */

            /*
             *   Logic for getting current user blogs, blogs count, followers count, following count, follow status
             */

            /* ------------------------- */
            req.setAttribute("User",user);
            req.getRequestDispatcher(USER_PAGE_PATH).forward(req,res);
            return;
        }

        /*
         *  Logic for user following/followers response
         */
        if(followIdentificator.equals(FOLLOWING_URL_IDENTIFICATOR)){
            /*
             *   Logic for getting followings
             */
        } else if (followIdentificator.equals(FOLLOWERS_URL_IDENTIFICATOR)){
            /*
             *   Logic for getting followers
             */
        }

        /*
         *  Logic for unknown follow identificator
         */
        res.sendError(res.SC_NOT_FOUND);
    }
}
