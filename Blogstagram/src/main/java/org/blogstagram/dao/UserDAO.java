package org.blogstagram.dao;

import org.blogstagram.models.User;

import javax.ws.rs.DELETE;
import java.sql.*;

public class UserDAO {
    Connection connection;


    private static final String ADD_USER_QUERY = "INSERT INTO users(firstname,lastname,nickname,users.role,email,password,birthday,gender,privacy,image,country,city,website,bio) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE users.id = ? OR users.nickname = ? OR users.email = ?";

    private static final String UPDATE_USER_GENERAL_INFO_QUERY = "UPDATE users SET firstname = ?,lastname = ?,nickname = ?,users.role = ?,email = ?,birthday = ?,gender = ?,privacy = ?,country = ?,city = ?,website = ?,bio = ? " +
            "WHERE id = ? OR nickname = ? OR email = ?";
    private static final String UPDATE_USER_PICTURE_QUERY = "UPDATE users SET image = ? WHERE id = ? OR nickname = ? OR email = ?";

    private static final String UPDATE_USER_PASSWORD_QUERY = "UPDATE users SET password = ? WHERE id = ? OR email = ? OR nickname = ?";

    private static final String GET_USER_QUERY = "SELECT id,firstname,lastname,nickname,users.role,email,birthday,gender,privacy,image,country,city,website,bio,created_at " +
            "FROM users WHERE id = ? OR nickname = ? OR email = ?";

    private static final String USER_KEYS_NULL_ERROR = "UserID or UserNickname or UserEmail must be included";
    private static final Integer NO_USER_ID = -1;

    public UserDAO(Connection connection){
        this.connection = connection;
    }

    private User getUser(Integer userID,String userNickname, String userEmail) throws SQLException {
        if(userID == null && userNickname == null && userEmail == null)
            throw new RuntimeException(USER_KEYS_NULL_ERROR);

        PreparedStatement stm = connection.prepareStatement(GET_USER_QUERY);
        if(userID == null)
            userID = NO_USER_ID;
        stm.setInt(1,userID);
        stm.setString(2,userNickname);
        stm.setString(3, userEmail);
        ResultSet result = stm.executeQuery();
        if(result.next()){
            Integer id = result.getInt(1);
            String firstname = result.getString(2);
            String lastname = result.getString(3);
            String nickname = result.getString(4);
            String role = result.getString(5);
            String email = result.getString(6);
            Date birthday = result.getDate(7);
            Integer gender = result.getInt(8);
            Integer privacy  = result.getInt(9);
            String image = result.getString(10);
            String country = result.getString(11);
            String city = result.getString(12);
            String website = result.getString(13);
            String bio = result.getString(14);
            Date createdAt = result.getDate(15);

            User user = new User(id,firstname,lastname,nickname,role,email,gender,privacy,birthday,image,country,city,website,bio,createdAt);
            return user;
        }

        return null;
    }
    public User getUserByID(Integer userID) throws SQLException {
        return getUser(userID,null, null);
    }
    public User getUserByNickname(String userNickname) throws SQLException {
        return getUser(null,userNickname, null);
    }
    public User getUserByEmail(String email) throws SQLException {
        return getUser(null, null, email);
    }

    public void addUser(User user,String password) throws SQLException {
        if(user.getId() != null)
            throw new RuntimeException("User with ID " + user.getId() + " Already Exists");

        PreparedStatement stm = connection.prepareStatement(ADD_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
        stm.setString(1,user.getFirstname());
        stm.setString(2,user.getLastname());
        stm.setString(3,user.getNickname());
        stm.setString(4,user.getRole());
        stm.setString(5,user.getEmail());
        stm.setString(6,password);
        stm.setDate(7, (Date) user.getBirthday());
        stm.setInt(8,user.getGender());
        stm.setInt(9,user.getPrivacy());
        stm.setString(10,user.getImage());
        stm.setString(11,user.getCountry());
        stm.setString(12,user.getCity());
        stm.setString(13,user.getWebsite());
        stm.setString(14,user.getBio());

        int affectedRows = stm.executeUpdate();
        if(affectedRows == 0)
            throw new SQLException("Creating user failed, could not insert in database");
        ResultSet generatedKeys = stm.getGeneratedKeys();
        if(generatedKeys.next()){
            user.setId(generatedKeys.getInt(1));
        }
        else
            throw new SQLException("Creating user failed, ID generation failed");
    }
    private void updateUserGeneralInfo(Integer userID,String userNickname, String userEmail,User user) throws SQLException {


        if(userID == null && userNickname == null && userEmail == null)
            throw new RuntimeException(USER_KEYS_NULL_ERROR);
        if(user == null)
            throw new RuntimeException("User general information must be included");

        if(userID == null)
            userID = NO_USER_ID;

        PreparedStatement stm = connection.prepareStatement(UPDATE_USER_GENERAL_INFO_QUERY);
        stm.setString(1,user.getFirstname());
        stm.setString(2,user.getLastname());
        stm.setString(3,user.getNickname());
        stm.setString(4,user.getRole());
        stm.setString(5,user.getEmail());
        stm.setDate(6, (Date) user.getBirthday());
        stm.setInt(7,user.getGender());
        stm.setInt(8,user.getPrivacy());
        stm.setString(9,user.getCountry());
        stm.setString(10,user.getCity());
        stm.setString(11,user.getWebsite());
        stm.setString(12,user.getBio());
        stm.setInt(13,userID);
        stm.setString(14,userNickname);
        stm.setString(15,userEmail);

        stm.executeUpdate();
    }
    public void updateUserGeneralInfoByID(Integer userID,User user) throws SQLException {
        updateUserGeneralInfo(userID,null, null,user);
    }
    public void updateUserGeneralInfoByNickname(String userNickname,User user) throws SQLException {
        updateUserGeneralInfo(null,userNickname, null,user);
    }
    public void updateUserGeneralInfoByEmail(String email,User user) throws SQLException {
        updateUserGeneralInfo(null, null, email,user);
    }


    private void updateUserPassword(Integer userID,String userNickname,String userEmail,String password) throws SQLException {
        if(userID == null && userNickname == null && userEmail == null)
            throw new RuntimeException(USER_KEYS_NULL_ERROR);

        if(userID == null)
            userID = NO_USER_ID;

        PreparedStatement stm = connection.prepareStatement(UPDATE_USER_PASSWORD_QUERY);
        stm.setString(1,password);
        stm.setInt(2,userID);
        stm.setString(3,userEmail);
        stm.setString(4,userNickname);

        int affectedRows = stm.executeUpdate();
        if(affectedRows == 0)
            throw new SQLException("Updating user failed, could not update in database");

    }

    public void updateUserPasswordByID(Integer userID,String password) throws SQLException {
        updateUserPassword(userID,null,null,password);
    }
    public void updateUserPasswordByNickname(String userNickname,String password) throws SQLException {
        updateUserPassword(null,userNickname,null,password);
    }
    public void updateUserPasswordByEmail(String userEmail,String password) throws SQLException {
        updateUserPassword(null,null,userEmail,password);
    }

    private void updateUserImage(Integer userID,String userNickname,String userEmail,String image) throws SQLException {
        /*
                Must Be Implemented
         */
        if(userID == null && userNickname == null && userEmail == null)
            throw new RuntimeException(USER_KEYS_NULL_ERROR);
        if(userID == null)
            userID = NO_USER_ID;

        PreparedStatement stm = connection.prepareStatement(UPDATE_USER_PICTURE_QUERY);
        stm.setString(1,image);
        stm.setInt(2,userID);
        stm.setString(3,userNickname);
        stm.setString(4,userEmail);

        int affectedRows = stm.executeUpdate();
        if(affectedRows == 0)
            throw new SQLException("Updating user failed, could not update in database");
    }
    public void updateUserImageByID(Integer userID,String image) throws SQLException {
        updateUserImage(userID,null,null,image);
    }
    public void updateUserImageByNickname(String userNickname,String image) throws SQLException {
        updateUserImage(null,userNickname,null,image);
    }
    public void updateUserImageByEmail(String userEmail,String image) throws SQLException {
        updateUserImage(null,null,userEmail,image);
    }

    private void deleteUser(Integer userID,String userNickname,String userEmail) throws SQLException {
        if(userID == null && userNickname == null && userEmail == null)
            throw new RuntimeException(USER_KEYS_NULL_ERROR);
        if(userID == null)
            userID = NO_USER_ID;

        PreparedStatement stm = connection.prepareStatement(DELETE_USER_QUERY);
        stm.setInt(1,userID);
        stm.setString(2,userNickname);
        stm.setString(3,userEmail);

        stm.executeUpdate();

    }

    public void deleteUserByID(Integer userID) throws SQLException {
        deleteUser(userID,null, null);
    }
    public void deleteUserByNickname(String userNickname) throws SQLException {
        deleteUser(null,userNickname, null);
    }
    public void deleteUserByEmail(String email) throws SQLException {
        deleteUser(null, null, email);
    }

}