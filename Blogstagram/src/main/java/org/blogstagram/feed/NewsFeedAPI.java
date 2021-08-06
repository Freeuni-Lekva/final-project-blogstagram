package org.blogstagram.feed;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.UserProvidedBlog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsFeedAPI {

    public static final String GET_NEWS_FEED_QUERY = "SELECT blogs.id,users.firstname,users.lastname,users.image FROM blogs\n" +
            "\tINNER JOIN follows ON blogs.user_id = follows.to_user_id\n" +
            "    INNER JOIN users ON follows.to_user_id = users.id\n" +
            "    WHERE follows.from_user_id = ?\n" +
            "\t\tUNION\n" +
            "SELECT blogs.id,users.firstname,users.lastname,users.image FROM blogs\n" +
            "\tINNER JOIN users ON blogs.user_id = users.id;";

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

    public List<UserProvidedBlog> getNewsFeed() throws SQLException, DatabaseError, InvalidSQLQueryException {
        List<UserProvidedBlog> blogs = new ArrayList<>();
        PreparedStatement stm = connection.prepareStatement(GET_NEWS_FEED_QUERY);
        stm.setInt(1,currentUserID);

        ResultSet result = stm.executeQuery();
        while(result.next()){
            int blogID = result.getInt(1);
            Blog blog = blogDAO.getBlog(blogID);

            String userFirstname = result.getString(2);
            String userLastname = result.getString(3);
            String userImage = result.getString(4);


            UserProvidedBlog userProvidedBlog = new UserProvidedBlog(userFirstname,userLastname,userImage,blog);

            blogs.add(userProvidedBlog);
        }


        return blogs;
    }
}
