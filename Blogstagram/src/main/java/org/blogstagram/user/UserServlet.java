package org.blogstagram.user;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.followSystem.api.StatusCodes;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;
import org.blogstagram.pairs.StringPair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    public static final String USER_PAGE_PATH = "/jsp/user/userPage.jsp";
    public static final String FOLLOW_LIST_PAGE_PATH = "/jsp/user/followListPage.jsp";

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
        if(pathParts.length > 3)
            return null;

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
    private SqlBlogDAO getBlogDAO(HttpServletRequest req){
        SqlBlogDAO blogDAO = (SqlBlogDAO) req.getSession().getAttribute("blogDao");
        return blogDAO;
    }

    private User getUserByIdentificator(UserDAO userDAO,String userIdentificator){
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
        return user;
    }

    private boolean canFollowListBeShown(Integer currentUserID,User user,FollowApi followApi) throws DatabaseError {
        // If user is private
        if(user.getPrivacy().equals(User.PUBLIC))
            return true;
        //If user is ME
        if(user.getId().equals(currentUserID))
            return true;
        //If user is private and I am not logged in
        if(user.getPrivacy().equals(User.PRIVATE) && currentUserID == null)
            return false;

        //If user is private, I am logged in and i am following
        return followApi.alreadyFollowed(currentUserID,user.getId()) == StatusCodes.followed;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringPair urlPair = getPathIdentificator(req);
        if(urlPair == null){
            res.sendError(res.SC_NOT_FOUND);
            return;
        }
        /* DAO INITIALIZING */
        UserDAO userDAO = getUserDAO(req);
        SqlBlogDAO blogDAO = getBlogDAO(req);
        SqlFollowDao followDAO = getSqlFollowDAO(req);
        followDAO.setUserDao(userDAO);
        FollowApi followApi = new FollowApi();
        followApi.setFollowDao(followDAO);
        followApi.setUserDao(userDAO);

        /* GETTING CURRENT USER */
        String userIdentificator = urlPair.getKey();
        Integer currentUserID = (Integer)req.getSession().getAttribute("currentUserID");
        User user = getUserByIdentificator(userDAO,userIdentificator);
        if(user == null){
            res.sendError(res.SC_NOT_FOUND);
            return;
        }

        String followIdentificator = urlPair.getValue();

        if(followIdentificator == null){
            /*
             *  Logic for user page response
             */


            /* ------------------------- */

            /*
             *   Logic for getting current user blogs, blogs count, followers count, following count, follow status
             */
            Integer followersCount = null;
            Integer followingCount = null;
            Integer blogsCount = null;
            Integer followStatus = null;
            List<Blog> blogs = null;

            try {
                if(currentUserID != null)
                    followStatus = (currentUserID.equals(user.getId())) ? (-1) :  (followApi.alreadyFollowed(currentUserID,user.getId()));
            } catch (DatabaseError databaseError) {
                databaseError.printStackTrace();
            }

            try {
                Integer userID = user.getId();
                followersCount = followApi.getFollowersCount(userID);
                followingCount = followApi.getFollowingCount(userID);
                blogsCount = blogDAO.getAmountOfBlogsByUser(userID);
                blogs = blogDAO.getBlogsOfUser(user.getId());

            } catch (NotValidUserIdException | DatabaseError | InvalidSQLQueryException e) {
                e.printStackTrace();
            }

            /* ------------------------- */
            req.setAttribute("User",user);
            req.setAttribute("FollowingCount",followingCount);
            req.setAttribute("FollowersCount",followersCount);
            req.setAttribute("BlogsCount",blogsCount);
            req.setAttribute("Blogs",blogs);
            req.setAttribute("FollowStatus",followStatus);
            req.getRequestDispatcher(USER_PAGE_PATH).forward(req,res);
            return;
        }

        /*
         *  Logic for user following/followers response
         */
        if(followIdentificator.equals(FOLLOWING_URL_IDENTIFICATOR)){

            try {
                if(!canFollowListBeShown(currentUserID,user,followApi)){
                    res.sendRedirect("/user/"+userIdentificator);
                    return;
                }
            } catch (DatabaseError databaseError) {
                databaseError.printStackTrace();
            }


            List<User> followingsList = null;
            try {
                followingsList = followApi.getAllFollowing(user.getId());
            } catch (NotValidUserIdException | DatabaseError e) {
                e.printStackTrace();
            }

            req.setAttribute("UserNickname",user.getNickname());
            req.setAttribute("Users",followingsList);
            req.setAttribute("FollowStatus","Followings");


            req.getRequestDispatcher(FOLLOW_LIST_PAGE_PATH).forward(req,res); return;
        } else if (followIdentificator.equals(FOLLOWERS_URL_IDENTIFICATOR)){
            try {
                if(!canFollowListBeShown(currentUserID,user,followApi)){
                    res.sendRedirect("/user/"+userIdentificator);
                    return;
                }
            } catch (DatabaseError databaseError) {
                databaseError.printStackTrace();
            }

            List<User> followersList = null;
            try {
                followersList = followApi.getAllFollowers(user.getId());
            } catch (NotValidUserIdException | DatabaseError e) {
                e.printStackTrace();
            }
            req.setAttribute("UserNickname",user.getNickname());
            req.setAttribute("Users",followersList);
            req.setAttribute("FollowStatus","Followers");

            req.getRequestDispatcher(FOLLOW_LIST_PAGE_PATH).forward(req,res); return;
        }

        /*
         *  Logic for unknown follow identificator
         */
        res.sendError(res.SC_NOT_FOUND);
    }
}
