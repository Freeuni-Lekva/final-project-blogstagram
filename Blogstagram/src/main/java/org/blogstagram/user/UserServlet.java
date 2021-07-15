package org.blogstagram.user;

import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.models.User;
import org.blogstagram.pairs.StringPair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    public static final String USER_PAGE_PATH = "/jsp/user/userPage.jsp";
    public static final String FOLLOWING_URL_IDENTIFICATOR = "following";
    public static final String FOLLOWERS_URL_IDENTIFICATOR = "followers";

    /*
     * URL : /user/userIdentificator/followingIdentificator
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

    private SqlFollowDao getSqlFollowDAO(HttpServletRequest req){
        SqlFollowDao followDAO = (SqlFollowDao) req.getSession().getAttribute("SqlFollowDao");
        return followDAO;
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

        SqlFollowDao followDAO = getSqlFollowDAO(req);
        followDAO.setUserDao(userDAO);
        FollowApi followApi = new FollowApi();
        followApi.setFollowDao(followDAO);
        followApi.setUserDao(userDAO);

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
            Integer followersCount = null;
            Integer followingCount = null;
            try {
                Integer userID = user.getId();
                followersCount = followApi.getFollowersCount(userID);
                followingCount = followApi.getFollowingCount(userID);
            } catch (NotValidUserIdException e) {
                e.printStackTrace();
            } catch (DatabaseError databaseError) {
                databaseError.printStackTrace();
            }


            /* ------------------------- */
            req.setAttribute("User",user);
            req.setAttribute("FollowingCount",followingCount);
            req.setAttribute("FollowersCount",followersCount);
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
