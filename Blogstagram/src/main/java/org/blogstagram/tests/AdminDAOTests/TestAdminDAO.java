package org.blogstagram.tests.AdminDAOTests;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.StringHasher;
import org.blogstagram.dao.AdminDAO;
import org.blogstagram.dao.CommentDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.models.Comment;
import org.blogstagram.models.User;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.sql.*;
import java.util.Random;

import static org.junit.Assert.assertThrows;

public class TestAdminDAO extends TestCase {
    private Connection connection;
    private AdminDAO adminDAO;
    private UserDAO userDAO;
    private CommentDAO commentDAO;
    private Random random;
    private User admin;
    private User[] users;
    private int[] blog_ids;
    private static final int NUM_USERS = 10;
    private static final String EMPTY_USERS = "DELETE FROM users";
    private static final String ADD_BLOG = "INSERT INTO blogs(user_id ,title, content, created_at) values(?,?,?,?)";
    private static final String EMPTY_COMMENTS = "DELETE FROM comments";
    private static final String EMPTY_BLOGS = "DELETE FROM blogs";

    @Override
    protected void setUp() throws SQLException {
        BasicDataSource source = new BasicDataSource();
        source.setUsername("root");
        source.setPassword("lukitoclasher"); // local password
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb_test");
        try {
            this.connection = source.getConnection();
            adminDAO = new AdminDAO(connection);
            userDAO = new UserDAO(connection);
            commentDAO = new CommentDAO(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        random = new Random();
        addUsers();
    }

    @Override
    protected void tearDown() throws SQLException {
        Statement stm = connection.createStatement();
        stm.execute(EMPTY_USERS);
        Statement stm1 = connection.createStatement();
        stm1.execute(EMPTY_BLOGS);
        Statement stm2 = connection.createStatement();
        stm2.execute(EMPTY_COMMENTS);
    }

    private void addUsers() throws SQLException {
        admin = createDummyUser(-1, User.ADMIN_ROLE);
        users = new User[NUM_USERS];
        for(int i = 0; i < NUM_USERS; i++){
            users[i] = createDummyUser(i, User.DEFAULT_ROLE);
        }
    }

    private User createDummyUser(int identificator, String role) throws SQLException {
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

        User user = new User(null,firstname,lastname,nickname,role,email,gender,privacy,birthday,image,country,city,website,bio,createdAt);
        userDAO.addUser(user, StringHasher.hashString("Password12345"));
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

    public int addDummyComment(int userId, int blog_id) throws SQLException {
        Comment comment = new Comment(userId, blog_id, "dummyCom", new Date(System.currentTimeMillis()) );
        comment = commentDAO.addComment(comment);
        return comment.getComment_id();
    }

    @Test
    public void testDeleteComment() throws SQLException {
        int blog_id = createDummyBlog(users[0].getId());
        int [] comment_ids = new int[NUM_USERS];
        for(int i = 0; i < NUM_USERS; i++){
            comment_ids[i] = addDummyComment(users[i].getId(), blog_id);
        }
        for(int i = 0; i < NUM_USERS; i++){
            adminDAO.deleteComment(comment_ids[i]);
        }
        for(int i = 0; i < NUM_USERS; i++){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM comments WHERE id = ?");
            preparedStatement.setInt(1, comment_ids[i]);
            ResultSet resultSet = preparedStatement.executeQuery();
            assertFalse(resultSet.next());
        }
    }

    @Test
    public void testDeleteCommentException() throws SQLException{
        int blog_id = createDummyBlog(users[0].getId());
        final int comment_id = addDummyComment(users[0].getId(), blog_id);

        assertThrows(SQLException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                adminDAO.deleteComment(comment_id + 1);
            }
        });
        assertThrows(SQLException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                adminDAO.deleteComment(-1);
            }
        });
    }

    public void testDeleteBlog() throws SQLException {
        blog_ids = new int[NUM_USERS];
        for(int i = 0; i < NUM_USERS; i++){
            blog_ids[i] = createDummyBlog(users[i].getId());
        }
        for(int i = 0; i < NUM_USERS; i++){
            adminDAO.deleteBlog(blog_ids[i]);
        }
        for(int i = 0; i < NUM_USERS; i++){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM blogs WHERE id = ?");
            preparedStatement.setInt(1, blog_ids[i]);
            ResultSet resultSet = preparedStatement.executeQuery();
            assertFalse(resultSet.next());
        }
        for(int i = 0; i < NUM_USERS; i++){
            final int j = i;
            assertThrows(SQLException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    adminDAO.deleteBlog(blog_ids[j]);
                }
            });
        }
    }

    public void testDeleteBlogException(){
        assertThrows(SQLException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                adminDAO.deleteBlog(-1);
            }
        });
    }

    public void testMakeUserModerator() throws SQLException{
        for(int i = 0; i < NUM_USERS; i++){
            adminDAO.makeUserModer(users[i].getId());
        }
        for(int i = 0; i < NUM_USERS; i++){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ? AND role = ?");
            preparedStatement.setInt(1, users[i].getId());
            preparedStatement.setString(2, User.MODERATOR_ROLE);
            ResultSet resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());

            final int j = i;
            assertThrows(SQLException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    adminDAO.makeUserModer(users[j].getId() * 2);
                }
            });
        }
    }

    public void testMakeUserModeratorException() {
        assertThrows(SQLException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                adminDAO.deleteUser(-1);
            }
        });
    }

    public void testMakeModeratorUser() throws SQLException{
        for(int i = 0; i < NUM_USERS; i++){
            adminDAO.makeModerUser(users[i].getId());
        }
        for(int i = 0; i < NUM_USERS; i++){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ? AND role = ?");
            preparedStatement.setInt(1, users[i].getId());
            preparedStatement.setString(2, User.DEFAULT_ROLE);
            ResultSet resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());

            final int j = i;
            assertThrows(SQLException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    adminDAO.makeModerUser(users[j].getId() * 2);
                }
            });
        }
    }

    public void testMakeModeratorUserException() {
        assertThrows(SQLException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                adminDAO.makeModerUser(-1);
            }
        });
    }

    public void testEligibility() throws SQLException {
        assertTrue(adminDAO.isEligible(admin.getId()));
        for(int i = 0; i < NUM_USERS; i++){
            assertFalse(adminDAO.isEligible(users[i].getId()));
        }
        assertThrows(SQLException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                adminDAO.isEligible(-1);
            }
        });
    }

}
