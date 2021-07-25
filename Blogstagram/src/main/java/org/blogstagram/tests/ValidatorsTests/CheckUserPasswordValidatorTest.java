package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.StringHasher;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.models.User;
import org.blogstagram.validators.CheckUserPasswordValidator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class CheckUserPasswordValidatorTest extends TestCase {
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
        User user = createDummyUser(1);
        userDAO.addUser(user, StringHasher.hashString("Password12345"));

        CheckUserPasswordValidator validator = new CheckUserPasswordValidator(user.getId(),"Password12345",connection);
        assertTrue(validator.validate());
    }
    public void test2() throws SQLException {
        User user = createDummyUser(2);
        userDAO.addUser(user,StringHasher.hashString("Password12345"));

        CheckUserPasswordValidator validator = new CheckUserPasswordValidator(user.getId(),"Password1234",connection);
        assertFalse(validator.validate());

        assertEquals(1,validator.getErrors().size());
    }
    public void test3() throws SQLException {
        User user1 = createDummyUser(3);
        User user2 = createDummyUser(4);

        userDAO.addUser(user1,StringHasher.hashString("Password1"));
        userDAO.addUser(user2,StringHasher.hashString("Password2"));

        CheckUserPasswordValidator validator1 = new CheckUserPasswordValidator(user1.getId(),"Passsword2",connection);
        CheckUserPasswordValidator validator2 = new CheckUserPasswordValidator(user2.getId(),"Password1",connection);

        assertFalse(validator1.validate());
        assertFalse(validator2.validate());

        assertEquals(1,validator1.getErrors().size());
        assertEquals(1,validator2.getErrors().size());
    }

    @Override
    protected void tearDown() throws Exception {
        Statement stm = connection.createStatement();
        stm.execute(EMPTY_QUERY);
    }
}
