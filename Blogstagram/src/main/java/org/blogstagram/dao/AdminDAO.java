package org.blogstagram.dao;

import org.blogstagram.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdminDAO {
    Connection connection;

    private static final String DELETE_USER = "DELETE FROM users WHERE  id = ?";
    private static final String DELETE_COMMENT = "DELETE FROM comments WHERE id = ?";
    private static final String DELETE_BLOG = "DELETE FROM blogs WHERE id = ?";
    private static final String CHANGE_ROLE = "UPDATE users SET role = ? WHERE id = ?";


    /*
     Receives connection in constructor
     */
    public AdminDAO(Connection connection){
        this.connection = connection;
    }
    /*
     Receives user unique id in parameters
     deletes user
    * */
    public void deleteUser(int user_id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_USER);
        ps.setInt(1, user_id);
        int numRows = ps.executeUpdate();
        if(numRows != 1){
            throw new SQLException("Deleting user failed");
        }
    }
    /*
    Receives comment unique id in parameters
    deletes comment
     */
    public void deleteComment(int comment_id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_COMMENT);
        ps.setInt(1, comment_id);
        int numRows = ps.executeUpdate();
        if(numRows != 1){
            throw new SQLException("Deleting comment failed");
        }
    }
    /*
    Receives blog unique id in parameters
    deletes blog
    */
    public void deleteBlog(int blog_id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_BLOG);
        ps.setInt(1, blog_id);
        int numRows = ps.executeUpdate();
        if(numRows != 1){
            throw new SQLException("Deleting blog failed");
        }
    }
    /*
    Receives user unique id in parameters
    gives user moderator privileges
     */
    public void makeUserModer(int user_id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(CHANGE_ROLE);
        ps.setString(1, "Moderator");
        ps.setInt(2, user_id);
        int numRows = ps.executeUpdate();
        if(numRows != 1){
            throw new SQLException("Giving moderator privileges to user failed");
        }
    }
    /*
    Receives user unique id in parameters
    takes away moderator privileges
     */
    public void makeModerUser(int user_id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(CHANGE_ROLE);
        ps.setString(1, "User");
        ps.setInt(2, user_id);
        int numRows = ps.executeUpdate();
        if(numRows != 1){
            throw new SQLException("Taking moderator privileges from user failed");
        }
    }
    /*

     */
//    public List<Integer>

}
