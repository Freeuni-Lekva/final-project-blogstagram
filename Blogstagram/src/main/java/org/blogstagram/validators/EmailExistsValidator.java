package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmailExistsValidator implements Validator{
    private static final int EMAIL_EXISTS = 1;
    private final Connection connection;
    private final String email;
    private static final String NON_EXISTENT_EMAIL_ERROR = "Email doesn't exist";
    private List<GeneralError> errors;

    public EmailExistsValidator(Connection connection, String email) {
        this.connection = connection;
        this.email = email;
    }

    @Override
    public boolean validate() throws SQLException {
        errors = new ArrayList<>();
        String query = "SELECT COUNT(id) FROM users WHERE email = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        if(count != EMAIL_EXISTS) {
            errors.add(new VariableError("email", NON_EXISTENT_EMAIL_ERROR));
        }
        return count == EMAIL_EXISTS;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}