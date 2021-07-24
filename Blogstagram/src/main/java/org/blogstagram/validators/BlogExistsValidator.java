package org.blogstagram.validators;

import org.blogstagram.dao.CommentDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlogExistsValidator implements CommentSystemValidator{

    private static final String BLOG_EXISTS = "SELECT * FROM blogs where id = ?";
    Connection connection;

    public void setConnection(Connection connection){

        this.connection = connection;
    }

    @Override
    public boolean validate(Object obj, Object obj1) throws SQLException {
        int blog_id = Integer.parseInt((String)obj);
        PreparedStatement ps = connection.prepareStatement(BLOG_EXISTS);
        ps.setInt(1, blog_id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }


}
