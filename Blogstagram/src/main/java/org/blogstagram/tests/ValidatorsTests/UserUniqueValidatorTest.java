package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.User;
import org.blogstagram.validators.UserUniqueValidator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class UserUniqueValidatorTest extends TestCase {


    public static final String EMPTY_QUERY = "DELETE FROM users";
    private Connection connection;
    private Random random = new Random();
    private UserDAO userDAO;

    @Override
    protected void setUp() throws Exception {
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

    public void test1() throws SQLException {
        UserUniqueValidator validator = new UserUniqueValidator(-1,"email@email.com","nickname",connection);
        assertTrue(validator.validate());
        assertEquals(0,validator.getErrors().size());
    }
    public void test2() throws SQLException {
        User user = createDummyUser(1);
        userDAO.addUser(user,"passwordos");
        UserUniqueValidator validator = new UserUniqueValidator(-1,user.getEmail(),null,connection);
        assertFalse(validator.validate());
        assertEquals(1,validator.getErrors().size());
        assertEquals("email",((VariableError)validator.getErrors().get(0)).getVariableName());
    }
    public void test3() throws SQLException {
        User user = createDummyUser(2);
        userDAO.addUser(user,"passwordos");
        UserUniqueValidator validator = new UserUniqueValidator(-1,null,user.getNickname(),connection);
        assertFalse(validator.validate());
        assertEquals(1,validator.getErrors().size());
        assertEquals("nickname",((VariableError)validator.getErrors().get(0)).getVariableName());
    }
    public void test4() throws SQLException {
        User user = createDummyUser(4);
        userDAO.addUser(user,"passwordos");
        UserUniqueValidator validator = new UserUniqueValidator(-1,user.getEmail(),user.getNickname(),connection);
        assertFalse(validator.validate());
        assertEquals(2,validator.getErrors().size());
    }
    public void test5() throws SQLException {
        User user = createDummyUser(5);
        userDAO.addUser(user,"passwordos");
        UserUniqueValidator validator = new UserUniqueValidator(user.getId(),user.getEmail(),user.getNickname(),connection);
        assertTrue(validator.validate());
        assertEquals(0,validator.getErrors().size());
    }
    public void test6() throws SQLException {
        User user1 = createDummyUser(6);
        User user2 = createDummyUser(7);
        userDAO.addUser(user1,"passwordos");
        userDAO.addUser(user2,"passwordos");

        UserUniqueValidator validator = new UserUniqueValidator(user1.getId(), user1.getEmail(),user2.getNickname(),connection);
        assertFalse(validator.validate());
        assertEquals(1,validator.getErrors().size());
        assertEquals("nickname",((VariableError)validator.getErrors().get(0)).getVariableName());
    }
    public void test7() throws SQLException {
        User user1 = createDummyUser(8);
        User user2 = createDummyUser(9);
        userDAO.addUser(user1,"passwordos");
        userDAO.addUser(user2,"passwordos");

        UserUniqueValidator validator = new UserUniqueValidator(user1.getId(), user2.getEmail(),user2.getNickname(),connection);
        assertFalse(validator.validate());
        assertEquals(2,validator.getErrors().size());
    }
    public void test8() throws SQLException {
        User user1 = createDummyUser(10);
        User user2 = createDummyUser(11);
        User user3 = createDummyUser(12);
        userDAO.addUser(user1,"passwordos");
        userDAO.addUser(user2,"passwordos");
        userDAO.addUser(user3,"passwordos");

        UserUniqueValidator validator = new UserUniqueValidator(user1.getId(), user2.getEmail(),user3.getNickname(),connection);
        assertFalse(validator.validate());
        assertEquals(2,validator.getErrors().size());
    }

    @Override
    protected void tearDown() throws Exception {
        Statement stm = connection.createStatement();
        stm.execute(EMPTY_QUERY);
    }
}
