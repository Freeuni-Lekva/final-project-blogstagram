package org.blogstagram.validators;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailExistsValidator {
    private static final int EMAIL_EXISTS = 1;
    private final Connection connection;
    private final String email;

    public EmailExistsValidator(Connection connection, String email) {
        this.connection = connection;
        this.email = email;
    }

    public boolean emailExists() throws SQLException {
        String query = "SELECT COUNT(id) FROM users WHERE email = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count == EMAIL_EXISTS;
    }

}