package org.blogstagram.followSystem.Validators;

import org.blogstagram.errors.NotLoggedInException;
import org.blogstagram.errors.NotValidUserIdException;

public class FollowRequestValidator {
    private String fromId;
    private String toId;
    private UserDAO userDao;

    public FollowRequestValidator(String fromId, String toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

    public void setUserDao(UserDAO userDao) throws NullPointerException {
        if (userDao == null) throw new NullPointerException("User Id can't be null.");
        this.userDao = userDao;
    }

    public boolean isFromIdValid() throws NotLoggedInException {
        if(fromId == null) throw new NotLoggedInException("User is not Logged in.");
        return true;
    }

    public boolean isToIdValid() throws NotValidUserIdException, NullPointerException {
        if (userDao == null) throw new NullPointerException("User Id can't be null.");
        UserIdValidator validator = new UserIdValidator();
        validator.setUserDao(userDao);
        return validator.validate(toId);
    }

}
