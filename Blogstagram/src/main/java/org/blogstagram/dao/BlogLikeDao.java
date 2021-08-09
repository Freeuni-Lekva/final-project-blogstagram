package org.blogstagram.dao;

import org.blogstagram.models.User;

import java.sql.*;
import java.util.ArrayList;

import static org.blogstagram.blogLike.blogLikeConstants.BlogLikeQueries.*;


public class BlogLikeDao {
    private final static int UNNECESSARY_INFO = -1;
    Connection connection;

    public BlogLikeDao(Connection connection) {
        this.connection = connection;
    }

    public void likeBlog(int blog_id, int user_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(LIKE_BLOG, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, user_id);
        statement.setInt(2, blog_id);
        statement.setDate(3, new Date(System.currentTimeMillis()));
        statement.executeUpdate();
    }

    public void  unlikeBlog(int blog_id, int user_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UNLIKE_BLOG);
        statement.setInt(1, blog_id);
        statement.setInt(2, user_id);
        statement.executeUpdate();
    }

    public ArrayList<User> getBlogLikers(int blog_id) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(BLOG_LIKES);
        statement.setInt(1, blog_id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Integer id = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String nickname = resultSet.getString(4);
            String image = resultSet.getString(5);

            int userId = id;
            String email = null;
            String role = null;
            String password = null;
            Date birthDate = null;
            int gender = UNNECESSARY_INFO;
            int privacy = UNNECESSARY_INFO;
            String country = null;
            String city = null;
            String website = null;
            String bio = null;
            Date createdAt = null;

            User currentUser = new User(userId, firstName, lastName, nickname, role, email,
                    gender, privacy, birthDate, image, country, city, website,  bio, createdAt);

            users.add(currentUser);
        }

        return users;
    }


    public int getLikeCount(int blog_id) throws SQLException{
        return getBlogLikers(blog_id).size();
    }
}

