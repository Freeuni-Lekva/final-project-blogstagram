package org.blogstagram.validators;

import org.blogstagram.dao.CommentDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentDeleteValidator implements CommentSystemValidator{
    Connection connection;
    private static final String COMMENT_EXISTS = "SELECT * FROM comments WHERE id = ? AND user_id = ?";

    public void setConnection(Connection connection) {

        this.connection = connection;
    }


    @Override
    public boolean validate(Object obj1, Object obj2) throws SQLException {
        int comment_id = (int)obj1;
        int user_id = (Integer) obj2;
        PreparedStatement ps = connection.prepareStatement(COMMENT_EXISTS);
        ps.setInt(1, comment_id);
        ps.setInt(2, user_id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
