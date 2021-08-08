package org.blogstagram.dao;

import org.blogstagram.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private Connection connection;
    private UserDAO userDAO;
    private static final String DELETE_USER = "DELETE FROM users WHERE  id = ?";
    private static final String DELETE_COMMENT = "DELETE FROM comments WHERE id = ?";
    private static final String DELETE_BLOG = "DELETE FROM blogs WHERE id = ?";
    private static final String CHANGE_ROLE = "UPDATE users SET role = ? WHERE id = ?";
    private static final String GET_BLOG_REPORTS = "SELECT comment FROM reports WHERE on_blog_id = ?";
    private static final String GET_USER_REPORTS = "SELECT comment FROM reports WHERE on_user_id = ?";
    private static final String GET_USER_ROLE = "SELECT role FROM users WHERE id = ?";
    /*
     Receives connection in constructor
     */
    public AdminDAO(Connection connection, UserDAO userDAO){
        this.userDAO = userDAO;
        this.connection = connection;
    }
    /*
     Receives user unique id in parameters
     deletes user
    * */
    public void deleteUser(int user_id) throws SQLException {
        userDAO.deleteUserByID(user_id);
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
        ps.setString(1, User.MODERATOR_ROLE);
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
        ps.setString(1, User.DEFAULT_ROLE);
        ps.setInt(2, user_id);
        int numRows = ps.executeUpdate();
        if(numRows != 1){
            throw new SQLException("Taking moderator privileges from user failed");
        }
    }
    /*
    Receives blog unique id in parameters
    returns list of report ids of the blog
     */
    public List<String> getBlogReports(int blog_id) throws SQLException {
        List<String> resultList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(GET_BLOG_REPORTS);
        preparedStatement.setInt(1, blog_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            String reportComment = resultSet.getString(1);
            resultList.add(reportComment);
        }
        return resultList;
    }
    /*
    Receives user unique id in parameters
    returns list of report ids of the user
     */
    public List<String> getUserReports(int comment_id) throws SQLException {
        List<String> resultList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_REPORTS);
        preparedStatement.setInt(1, comment_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            String reportComment = resultSet.getString(1);
            resultList.add(reportComment);
        }
        return resultList;
    }
    /*
    Receives user id in parameters
    returns if it is admin
     */
    public boolean isEligibleToChangeRole(int user_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ROLE);
        preparedStatement.setInt(1, user_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        String user_role = "";
        if(resultSet.next()){
            user_role = resultSet.getString(1);
            return user_role.equals(User.ADMIN_ROLE);
        }
        throw new SQLException("User with that ID does not exist");
    }
    /*
    Receives user id in parameters
    returns if it is moderator or admin
    */
    public boolean isEligible(int user_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ROLE);
        preparedStatement.setInt(1, user_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        String user_role = "";
        if(resultSet.next()){
            user_role = resultSet.getString(1);
            return user_role.equals(User.ADMIN_ROLE) || user_role.equals(User.MODERATOR_ROLE);
        }
        throw new SQLException("User with that ID does not exist");
    }
    /*
    Receives user id in parameters
    returns if it is moderator
    */
    public boolean isModerator(int user_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ROLE);
        preparedStatement.setInt(1, user_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        String user_role = "";
        if(resultSet.next()){
            user_role = resultSet.getString(1);
            return user_role.equals(User.MODERATOR_ROLE);
        }
        throw new SQLException("User with that ID does not exist");
    }

}
