package org.blogstagram.Validators;

import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.dao.FollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.followSystem.api.StatusCodes;
import org.blogstagram.models.DirectedFollow;
import org.blogstagram.models.User;

import java.sql.SQLException;

public class GetUserConnectionsValidator {
    private UserDAO userDao;
    private FollowApi followApi;

    public void setApi(FollowApi api) {
        if(api == null) throw new NullPointerException("Follow api object can't be null.");
        this.followApi = api;
    }

    public void setUserDao(UserDAO userDao) {
        if(userDao == null) throw new NullPointerException("User dao object can't be null.");
        this.userDao = userDao;
    }



    public boolean validateRequest(String fromRequest, String toRequest) throws DatabaseError, NotValidUserIdException {
        UserIdValidator validator = new UserIdValidator();
        validator.setUserDao(userDao);
        if(validator.validate(toRequest) && validator.validate(fromRequest)){
            Integer from_id = Integer.parseInt(fromRequest);
            Integer to_id = Integer.parseInt(toRequest);
            if(from_id.equals(to_id)) return true;
            try {
                User toUser = userDao.getUserByID(to_id);
                User fromUser = userDao.getUserByID(from_id);
                if(toUser.getPrivacy().equals(User.PUBLIC)) return true;
                return followApi.alreadyFollowed(from_id, to_id) == StatusCodes.followed;
            } catch (SQLException exception) {
                throw new DatabaseError("Can't Connect to database");
            }
        }
        return false;
    }



}
