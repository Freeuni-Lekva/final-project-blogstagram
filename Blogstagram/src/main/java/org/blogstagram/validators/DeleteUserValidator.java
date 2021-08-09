package org.blogstagram.validators;

import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteUserValidator implements Validator{

    public static final String DELETE_USER_NO_ID_ERROR = "Not included ID of user that should be deleted";
    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    public static final String NO_ADMIN_OR_MODERATOR_ERROR = "You do not have permission to delete another user";

    private Integer currentUserID;
    private Integer deleteUserID;
    private UserDAO userDAO;

    private List<GeneralError> errors;

    public DeleteUserValidator(Integer currentUserID, Integer deleteUserID, UserDAO userDAO){
        if(userDAO == null)
            throw new IllegalArgumentException("UserDAO object should not be null");
        this.currentUserID = currentUserID;
        this.deleteUserID = deleteUserID;
        this.userDAO = userDAO;

        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() throws SQLException {
        errors = new ArrayList<>();
        /* If Delete User ID Is Not Included */
        if(deleteUserID == null){
            errors.add(new VariableError("deleteUserID",DELETE_USER_NO_ID_ERROR));
            return false;
        }
        /* If ID is included and is the same user that's logged in */
        if(currentUserID.equals(deleteUserID))
            return true;

        /* If deleteUserID Does not exist */
        User deleteUser = userDAO.getUserByID(deleteUserID);
        if(deleteUser == null){
            errors.add(new VariableError("deleteUserID",USER_DOES_NOT_EXIST));
            return false;
        }
        /* If deleteUserID exists and logged user is not moderator/admin */
        User currentUser = userDAO.getUserByID(currentUserID);
        if(!currentUser.getRole().equals(User.MODERATOR_ROLE) && !currentUser.getRole().equals(User.ADMIN_ROLE))
            errors.add(new VariableError("currentUserID",NO_ADMIN_OR_MODERATOR_ERROR));

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
