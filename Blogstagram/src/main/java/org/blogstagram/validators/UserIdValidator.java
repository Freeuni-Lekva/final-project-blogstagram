package org.blogstagram.validators;

import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.models.User;

import java.sql.SQLException;

public class UserIdValidator implements followValidator {
    private UserDAO userDao;

    public void setUserDao(UserDAO userDao) {
        if (userDao == null) throw new NullPointerException("User dao object can't be null.");
        this.userDao = userDao;
    }

    @Override
    public boolean validate(Object obj) throws NotValidUserIdException, NullPointerException, DatabaseError {
        if(userDao == null) throw new NullPointerException("User dao object can't be null.");

        try {
            Integer toId;
            if(obj instanceof String)  toId = Integer.parseInt((String)obj);
            else toId = (Integer) obj;
            User toIdUser = userDao.getUserByID(toId);
            if(toIdUser == null) throw new NotValidUserIdException("User id is not registered.");
        } catch (NumberFormatException ex){
            throw new NotValidUserIdException("User Id should contain only Numbers.");
        } catch (SQLException ex){
            throw new DatabaseError("Can't accses database");
        }
        return true;
    }
}
