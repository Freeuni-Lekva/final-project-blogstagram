package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdminCommentValidator implements Validator{
    Connection connection;
    int comment_id;
    private static final String COMMENT_EXISTS = "SELECT * FROM comments WHERE id = ?";

    public void setConnectionComment(Connection connection, int comment_id) {
        this.connection = connection;
        this.comment_id = comment_id;
    }

    @Override
    public boolean validate() throws SQLException {
        PreparedStatement ps = connection.prepareStatement(COMMENT_EXISTS);
        ps.setInt(1, comment_id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    @Override
    public List<GeneralError> getErrors() {
        return null;
    }
}
