package org.blogstagram.validators;

import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.NotLoggedInException;
import org.blogstagram.errors.NotValidUserIdException;


///// change design

public class FollowRequestValidator {
    private UserDAO userDao;



    public void setUserDao(UserDAO userDao) throws NullPointerException {
        if (userDao == null) throw new NullPointerException("User Dao object can't be null.");
        this.userDao = userDao;
    }

    public boolean isLoggedIn(String id) throws NotLoggedInException {
        if(id == null) throw new NotLoggedInException("User is not Logged in.");
        return true;
    }

    public boolean isIdValid(String id) throws NotValidUserIdException, NullPointerException, DatabaseError {
        if (userDao == null) throw new NullPointerException("User dao can't be null.");
        UserIdValidator validator = new UserIdValidator();
        validator.setUserDao(userDao);
        return validator.validate(id);
    }

}
