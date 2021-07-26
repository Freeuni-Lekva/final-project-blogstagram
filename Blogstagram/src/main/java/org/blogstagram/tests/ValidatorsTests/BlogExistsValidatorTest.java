package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.StringHasher;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.models.User;
import org.blogstagram.validators.BlogExistsValidator;
import org.junit.Test;

import java.sql.*;
import java.util.Random;

public class BlogExistsValidatorTest extends TestCase {
    BlogExistsValidator blogValidator;
    private Connection connection;
    private UserDAO userDAO;
    private Random random = new Random();

    private static final String EMPTY_USERS = "DELETE FROM users";
    private static final String EMPTY_BLOGS = "DELETE FROM blogs";
    private static final String ADD_BLOG = "INSERT INTO blogs(user_id ,title, content, created_at) values(?,?,?,?)";


    @Override
    protected void setUp() throws Exception {
        BasicDataSource source = new BasicDataSource();
        source.setUsername("root");
        source.setPassword(""); // local password
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb_test");
        try {
            this.connection = source.getConnection();
            userDAO = new UserDAO(connection);
            blogValidator = new BlogExistsValidator();
            blogValidator.setConnection(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        Statement stm = connection.createStatement();
        stm.execute(EMPTY_USERS);
        Statement stm1 = connection.createStatement();
        stm1.execute(EMPTY_BLOGS);
    }

    public User createDummyUser(int identificator){
        String firstname = "firstname" + String.valueOf(identificator);
        String lastname = "lastname" + String.valueOf(identificator);
        String nickname = "nickname" + String.valueOf(identificator);
        String email = "email" + String.valueOf(identificator)+"@gmail.com";
        Date birthday = Date.valueOf("2001-08-07");
        Integer gender = random.nextInt(2);
        Integer privacy = random.nextInt(2);
        String image = "image" + String.valueOf(identificator);
        String country = "Georgia";
        String city = "Tbilisi";
        String website = "website" + String.valueOf(identificator);
        String bio ="Xelou Gaiz " + String.valueOf(identificator);
        Date createdAt = new Date(System.currentTimeMillis());

        User user = new User(null,firstname,lastname,nickname,User.DEFAULT_ROLE,email,gender,privacy,birthday,image,country,city,website,bio,createdAt);
        return user;
    }

    private int createDummyBlog(int user_id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(ADD_BLOG, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1,user_id);
        ps.setString(2, "dummy_title");
        ps.setString(3, "dummy_content");
        ps.setDate(4, new Date(System.currentTimeMillis()));
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if(generatedKeys.next()){
            return generatedKeys.getInt(1);
        }
        return -1;
    }

    @Test
    public void testTrivial() throws SQLException {
        User user1 = createDummyUser(1);
        userDAO.addUser(user1, StringHasher.hashString("Password12345"));
        String blog_id = String.valueOf(createDummyBlog(user1.getId()));
        assertTrue(blogValidator.validate(blog_id,""));
        assertFalse(blogValidator.validate(blog_id+1, ""));
    }

    @Test
    public void testNonExistent() throws SQLException {
        assertFalse(blogValidator.validate("-1",""));
        assertFalse(blogValidator.validate("0", ""));
    }

    @Test
    public void testCyclic() throws SQLException {
        for(int i = 2; i <=10; i++){
            User user = createDummyUser(i);
            userDAO.addUser(user, StringHasher.hashString("Password12345"));
            String blog_id = String.valueOf(createDummyBlog(user.getId()));
            assertTrue(blogValidator.validate(blog_id,""));
            assertFalse(blogValidator.validate(blog_id+1, ""));
        }
    }

}
