package org.blogstagram.validators;

import org.blogstagram.dao.AdminDAO;
import org.blogstagram.errors.GeneralError;

import java.sql.SQLException;
import java.util.List;

public class AdminValidator implements Validator{
    private AdminDAO adminDAO;
    private int user_id;
    private boolean needsAdmin;

    public void setAdminDAOUser(AdminDAO adminDAO, int user_id, boolean needsAdmin){
        this.adminDAO = adminDAO;
        this.user_id = user_id;
        this.needsAdmin = needsAdmin;
    }

    @Override
    public boolean validate() throws SQLException {
        if(needsAdmin){
            return adminDAO.isEligibleToChangeRole(user_id);
        }else {
            return adminDAO.isEligible(user_id);
        }
    }

    @Override
    public List<GeneralError> getErrors() {
        return null;
    }
}
