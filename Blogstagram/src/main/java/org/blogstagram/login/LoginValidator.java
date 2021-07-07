package org.blogstagram.login;

import org.blogstagram.StringHasher;

import java.sql.*;

public class LoginValidator {
    public static final String NON_EXISTENT_EMAIL_ERROR = "Email doesn't exist";
    public static final String INCORRECT_PASSWORD_ERROR = "Password is incorrect";
    private static final int EMAIL_EXISTS = 1;

    private final String email;
    private final String password;

    public LoginValidator(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean emailExists(Connection connection) throws SQLException {
        String query = "SELECT COUNT(id) FROM users WHERE email = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count == EMAIL_EXISTS;
    }

    public boolean passwordMatch(Connection connection) throws SQLException {
        if(!emailExists(connection))
            return false;
        String hashCode = StringHasher.hashString(this.password);
        String query = "SELECT password FROM users WHERE email = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        String correctPassword = resultSet.getString(1);
        return hashCode.equals(correctPassword);
    }

}
