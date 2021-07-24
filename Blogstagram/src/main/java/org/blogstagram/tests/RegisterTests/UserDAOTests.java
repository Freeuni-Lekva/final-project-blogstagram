package org.blogstagram.tests.RegisterTests;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.StringHasher;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.models.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.sql.*;
import java.util.Random;

public class UserDAOTests extends TestCase {

    private static final String EMPTY_QUERY = "DELETE FROM users";

    private UserDAO userDAO;
    private Random random = new Random();
    private Connection connection;

    @Override
    public void setUp(){
        BasicDataSource source = new BasicDataSource();
        source.setUsername("root");
        source.setPassword(""); // local password
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb_test");
        try {
            this.connection = source.getConnection();
            userDAO = new UserDAO(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public User createDummyUser(int identificator){

        String firstname = "firstname"+String.valueOf(identificator);
        String lastname = "lastname"+String.valueOf(identificator);
        String nickname = "nickname"+String.valueOf(identificator);
        String email = "email"+String.valueOf(identificator)+"@gmail.com";
        Date birthday = Date.valueOf("2001-08-07");
        Integer gender = random.nextInt(2);
        Integer privacy = random.nextInt(2);
        String image = "image"+String.valueOf(identificator);
        String country = "Georgia";
        String city = "Tbilisi";
        String website = "website"+String.valueOf(identificator);
        String bio ="Xelou Gaiz "+String.valueOf(identificator);
        Date createdAt = new Date(System.currentTimeMillis());

        User user = new User(null,firstname,lastname,nickname,User.DEFAULT_ROLE,email,gender,privacy,birthday,image,country,city,website,bio,createdAt);
        return user;
    }

    public String getPasswordByID(Integer id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT password FROM users WHERE id = ?");
        stm.setInt(1,id);

        ResultSet result = stm.executeQuery();

        if(result.next())
            return result.getString(1);
        return null;

    }
    public String getImageByID(Integer id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT image FROM users WHERE id = ?");
        stm.setInt(1,id);

        ResultSet result = stm.executeQuery();

        if(result.next())
            return result.getString(1);
        return null;
    }

    public void test1() throws SQLException {
        User user = createDummyUser(1);
        userDAO.addUser(user,"Passwordos");

        User dbuser = userDAO.getUserByID(user.getId());

        assertEquals(user.getNickname(),dbuser.getNickname());
        assertEquals(user.getEmail(),dbuser.getEmail());
    }
    public void test2() throws SQLException {
        User user = createDummyUser(2);
        userDAO.addUser(user,"Passwordos");

        User dbuser = userDAO.getUserByEmail(user.getEmail());

        assertEquals(user.getId(),dbuser.getId());
        assertEquals(user.getNickname(),dbuser.getNickname());
    }
    public void test3() throws SQLException {
        User user = createDummyUser(3);
        userDAO.addUser(user,"Passwordos");

        User dbuser = userDAO.getUserByNickname(user.getNickname());

        assertEquals(user.getId(),dbuser.getId());
        assertEquals(user.getEmail(),dbuser.getEmail());
    }

    public void test4() throws SQLException {
        User user = createDummyUser(4);
        userDAO.addUser(user,"Passwordos");

        user.setCountry("Alabama");
        userDAO.updateUserGeneralInfoByID(user.getId(),user);

        User dbuser = userDAO.getUserByID(user.getId());

        assertEquals("Alabama",dbuser.getCountry());

    }

    public void test5() throws SQLException {
        User user = createDummyUser(5);
        userDAO.addUser(user,"Passwordos");

        user.setCity("Maiami");
        userDAO.updateUserGeneralInfoByNickname(user.getNickname(),user);

        User dbuser = userDAO.getUserByNickname(user.getNickname());

        assertEquals("Maiami",dbuser.getCity());
    }

    public void test6() throws SQLException {
        User user = createDummyUser(6);
        userDAO.addUser(user,"Passwordos");

        user.setWebsite("websitos");
        userDAO.updateUserGeneralInfoByEmail(user.getEmail(),user);

        User dbuser = userDAO.getUserByEmail(user.getEmail());

        assertEquals("websitos",dbuser.getWebsite());
    }

    public void test7() throws SQLException {
        User user = createDummyUser(7);
        String password = StringHasher.hashString("Test1234");

        userDAO.addUser(user,password);

        String newPassword = StringHasher.hashString("Test12345");
        userDAO.updateUserPasswordByID(user.getId(),newPassword);

        String dbPassword = getPasswordByID(user.getId());

        assertEquals(newPassword,dbPassword);
    }
    public void test8() throws SQLException {
        User user = createDummyUser(8);
        String password = StringHasher.hashString("Test1234");

        userDAO.addUser(user,password);

        String newPassword = StringHasher.hashString("Test12345");
        userDAO.updateUserPasswordByNickname(user.getNickname(),newPassword);

        String dbPassword = getPasswordByID(user.getId());

        assertEquals(newPassword,dbPassword);
    }
    public void test9() throws SQLException {
        User user = createDummyUser(9);
        String password = StringHasher.hashString("Test1234");

        userDAO.addUser(user,password);

        String newPassword = StringHasher.hashString("Test12345");
        userDAO.updateUserPasswordByEmail(user.getEmail(),newPassword);

        String dbPassword = getPasswordByID(user.getId());

        assertEquals(newPassword,dbPassword);
    }

    public void test10() throws SQLException {
        User user = createDummyUser(10);
        userDAO.addUser(user,"Passwordos");

        String newImage = "ImagosTalontos.png";
        userDAO.updateUserImageByID(user.getId(),newImage);

        String dbImage = getImageByID(user.getId());

        assertEquals(newImage,dbImage);
    }
    public void test11() throws SQLException {
        User user = createDummyUser(11);
        userDAO.addUser(user,"Passwordos");

        String newImage = "ImagosTalontos.png";
        userDAO.updateUserImageByNickname(user.getNickname(),newImage);

        String dbImage = getImageByID(user.getId());

        assertEquals(newImage,dbImage);
    }
    public void test12() throws SQLException {
        User user = createDummyUser(12);
        userDAO.addUser(user,"Passwordos");

        String newImage = "ImagosTalontos.png";
        userDAO.updateUserImageByEmail(user.getEmail(),newImage);

        String dbImage = getImageByID(user.getId());

        assertEquals(newImage,dbImage);
    }

    @Override
    protected void tearDown() throws Exception {
        Statement stm = connection.createStatement();
        stm.execute(EMPTY_QUERY);
    }

}
