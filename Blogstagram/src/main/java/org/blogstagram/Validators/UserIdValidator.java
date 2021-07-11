package org.blogstagram.Validators;

import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.NotValidUserIdException;

public class UserIdValidator implements Validator {
    private UserDAO userDao;

    public void setUserDao(UserDAO userDao) {
        if (userDao == null) throw new NullPointerException("User dao object can't be null.");
        this.userDao = userDao;
    }

    @Override
    public boolean validate(Object obj) throws NotValidUserIdException, NullPointerException {
        if(userDao == null) throw new NullPointerException("User dao object can't be null.");
        String toIdStr = (String) obj;
        try {
            Integer toId = Integer.parseInt(toIdStr);
            //User toIdUser = userDao.getUserByIdOrNickname(toId, null);
            //if(toIdUser == null) throw new NotValidUserIdException("User id is not registered.");
        } catch (NumberFormatException ex){
            throw new NotValidUserIdException("User Id should contain only Numbers.");
        }
        return true;
    }
}
