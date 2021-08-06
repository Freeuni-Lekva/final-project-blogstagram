package org.blogstagram.feed;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsFeedAPI {

    public static final String GET_NEWS_FEED_QUERY = "SELECT blogs.id FROM blogs INNER JOIN follows ON follows.to_user_id = blogs.user_id INNER \tJOIN users ON follows.from_user_id = users.id WHERE users.id = ?\n" +
            "\tUNION\n" +
            "SELECT blogs.id FROM blogs INNER JOIN users ON blogs.user_id = users.id;";

    private static final Integer NO_USER_ID = -1;

    private Integer currentUserID;
    private Connection connection;
    private SqlBlogDAO blogDAO;

    public NewsFeedAPI(Integer currentUserID,SqlBlogDAO blogDAO,Connection connection){
        if(connection == null)
            throw new IllegalArgumentException("Connection should not be null");
        if(blogDAO == null)
            throw new IllegalArgumentException("BlogDAO should not be null");

        this.currentUserID = (currentUserID == null) ? (NO_USER_ID) : (currentUserID);
        this.connection = connection;
        this.blogDAO = blogDAO;
    }
    public NewsFeedAPI(SqlBlogDAO blogDAO,Connection connection){
        this(NO_USER_ID,blogDAO,connection);
    }

    public List<Blog> getNewsFeed() throws SQLException, DatabaseError, InvalidSQLQueryException {
        List<Blog> blogs = new ArrayList<>();
        PreparedStatement stm = connection.prepareStatement(GET_NEWS_FEED_QUERY);
        stm.setInt(1,currentUserID);

        ResultSet result = stm.executeQuery();
        while(result.next()){
            int blogID = result.getInt(1);
            Blog blog = blogDAO.getBlog(blogID);
            blogs.add(blog);
        }


        return blogs;
    }
}
