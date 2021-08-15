package org.blogstagram.validators;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.followSystem.api.StatusCodes;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;

import java.sql.SQLException;

public class getBlogRequestValidator {
    private UserDAO userDAO;
    private FollowApi followApi;
    private Integer userid;
    private Blog currentBlog;

    public void setUserDAO(UserDAO userDAO) {
        if(userDAO == null) throw new NullPointerException("UserDao Object Can't be null.");
        this.userDAO = userDAO;
    }

    public void setFollowApi(FollowApi followApi) {
        if(followApi == null) throw new NullPointerException("FollowApi object can't be null");
        this.followApi = followApi;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setCurrentBlog(Blog currentBlog) {
        this.currentBlog = currentBlog;
    }

    public boolean shouldBeShown() throws DatabaseError {

        try {
            User account = userDAO.getUserByID(currentBlog.getUser_id());
            if(account == null) return false;
            else if(account.getPrivacy().equals(User.PUBLIC)) return true;
            else if(userid == null) return false;
            else if(currentBlog.getUser_id() == userid) return true;


            User visitor = userDAO.getUserByID(userid);

            if(visitor.getRole().equals(User.ADMIN_ROLE) || visitor.getRole().equals(User.MODERATOR_ROLE)) return true;

            return followApi.alreadyFollowed(userid, currentBlog.getUser_id()) == StatusCodes.followed;

        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to Database");
        }
    }



}
