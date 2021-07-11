package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;

import java.sql.*;
import java.util.List;

public class LoginValidator {

    private final UserExists userExists;

    public LoginValidator(String email, String password, Connection connection) {
        userExists = new UserExists(connection, email, password);
    }

    public boolean validation() throws SQLException {
        return userExists.exists();
    }

    public List<GeneralError> getErrors() {
        return userExists.getErrors();
    }

    public int getId() {
        return userExists.getId();
    }
}
