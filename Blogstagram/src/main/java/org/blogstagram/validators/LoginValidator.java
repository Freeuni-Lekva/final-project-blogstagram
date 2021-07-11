package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LoginValidator implements Validator {

    private final UserExistsValidator userExists;


    public LoginValidator(String email, String password, Connection connection) {
        userExists = new UserExistsValidator(connection, email, password);
    }

    @Override
    public boolean validate() throws SQLException {
        return userExists.exists();
    }

    @Override
    public List<GeneralError> getErrors() {
        return userExists.getErrors();
    }
}
