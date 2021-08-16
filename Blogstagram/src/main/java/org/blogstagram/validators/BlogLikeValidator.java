package org.blogstagram.validators;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlogLikeValidator {
    Connection connection;

    private static final String POST_LIKED_QUERY = "SELECT * from likes where blog_id = ? and user_id = ?";


    public void setConnection(Connection connection){

        this.connection = connection;
    }

    public boolean validate(Object obj1, Object obj2) throws SQLException {
        int post_id = Integer.parseInt((String)obj1);
        int user_id = (Integer)obj2;
        PreparedStatement statement = connection.prepareStatement(POST_LIKED_QUERY);
        statement.setInt(1, post_id);
        statement.setInt(2, user_id);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }
}
