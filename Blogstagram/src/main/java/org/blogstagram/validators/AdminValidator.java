package org.blogstagram.validators;

import org.blogstagram.dao.AdminDAO;
import org.blogstagram.errors.GeneralError;

import java.sql.SQLException;
import java.util.List;

public class AdminValidator implements Validator{
    private AdminDAO adminDAO;
    private int user_id;

    public void setAdminDAOUser(AdminDAO adminDAO, int user_id){
        this.adminDAO = adminDAO;
        this.user_id = user_id;
    }

    @Override
    public boolean validate() throws SQLException {
        return adminDAO.isEligible(user_id);
    }

    @Override
    public List<GeneralError> getErrors() {
        return null;
    }
}
