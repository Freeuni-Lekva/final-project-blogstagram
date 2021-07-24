package org.blogstagram.validators;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentLikeValidator{
    private static final String COMMENT_LIKED_QUERY = "SELECT * from likes where comment_id = ? and user_id = ?";
    Connection connection;
    public void setConnection(Connection connection){

        this.connection = connection;
    }

    public boolean validate(Object obj1, Object obj2) throws SQLException {
        int comment_id = Integer.parseInt((String)obj1);
        int user_id = Integer.parseInt((String)obj2);
        PreparedStatement ps = connection.prepareStatement(COMMENT_LIKED_QUERY);
        ps.setInt(1, comment_id);
        ps.setInt(2, user_id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
