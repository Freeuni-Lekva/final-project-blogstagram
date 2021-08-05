package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserNotModeratorValidator implements Validator{
    private Connection connection;
    private int user_id;
    private static final String GET_USER = "SELECT FROM users WHERE id = ? AND role = ?";

    public void setConnectionUser(Connection connection, int user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    @Override
    public boolean validate() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_USER);
        preparedStatement.setInt(1, user_id);
        preparedStatement.setString(2, User.DEFAULT_ROLE);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    @Override
    public List<GeneralError> getErrors() {
        return null;
    }
}
