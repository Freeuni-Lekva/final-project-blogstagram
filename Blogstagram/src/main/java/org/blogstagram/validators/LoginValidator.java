package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginValidator implements Validator {

    private final UserExistsValidator userExists;
    private List<GeneralError> errors;
    private final String email;
    private final String password;
    private static final String NOT_INCLUDED_EMAIL = "Email mustn't be empty";
    private static final String NOT_INCLUDED_PASSWORD = "Password mustn't be empty";

    public LoginValidator(String email, String password, Connection connection) {
        this.email = email;
        this.password = password;
        userExists = new UserExistsValidator(connection, email, password);
    }

    @Override
    public boolean validate() throws SQLException {
        errors = new ArrayList<>();
        if(email == null) {
            errors.add(new VariableError("email", NOT_INCLUDED_EMAIL));
        }
        if(password == null) {
            errors.add(new VariableError("password", NOT_INCLUDED_PASSWORD));
        }
        EmailFormatValidator emailFormatValidator = new EmailFormatValidator(email);
        if(!emailFormatValidator.validate()){
            errors.addAll(emailFormatValidator.getErrors());
        }
        if(!userExists.validate()) {
            errors.addAll(userExists.getErrors());
        }
        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
