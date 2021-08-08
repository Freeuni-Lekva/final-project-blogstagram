package org.blogstagram.dao;

import org.blogstagram.models.User;

import java.sql.*;
import java.util.ArrayList;

import static org.blogstagram.postLike.postLikeConstants.PostLikeQueries.*;

public class PostLikeDao {
    private final static int UNNECESSARY_INFO = -1;
    Connection connection;

    public PostLikeDao(Connection connection) {
        this.connection = connection;
    }

    public void likePost(int blog_id, int user_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(LIKE_POST, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, user_id);
        statement.setInt(2, blog_id);
        statement.setDate(3, new Date(System.currentTimeMillis()));
        statement.executeUpdate();
    }

    public void  unlikePost(int blog_id, int user_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UNLIKE_POST);
        statement.setInt(1, blog_id);
        statement.setInt(2, user_id);
        statement.executeUpdate();
    }

    public ArrayList<User> getPostLikers(int blog_id) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(POST_LIKES);
        statement.setInt(1, blog_id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String firstName = resultSet.getString(1);
            String lastName = resultSet.getString(2);
            String nickname = resultSet.getString(3);
            String image = resultSet.getString(4);

            int userId = UNNECESSARY_INFO;
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
        return getPostLikers(blog_id).size();
    }
}

