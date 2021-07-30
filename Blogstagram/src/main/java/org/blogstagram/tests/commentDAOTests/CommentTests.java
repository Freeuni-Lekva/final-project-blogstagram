package org.blogstagram.tests.commentDAOTests;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.dao.CommentDAO;
import org.blogstagram.models.Comment;
import org.blogstagram.models.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CommentTests extends TestCase {

    private Connection connection;
    CommentDAO commentDAO;
    // i-th blog created by i-th user
    int[] blog_ids;
    // users
    User[] users;
    public static final int NUM_USERS = 10;
    prepareData pd;

    @Override
    protected void setUp() throws Exception {
        BasicDataSource source = new BasicDataSource();
        source.setUsername("root");
        source.setPassword("lukitoclasher"); // local password
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb_test");
        try {
            this.connection = source.getConnection();
            pd = new prepareData(connection);
            commentDAO = new CommentDAO(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        this.users = pd.createUsers(NUM_USERS);
        this.blog_ids = pd.createBlogs();
    }

    @Override
    protected void tearDown() throws Exception {
        pd.cleanUpData();
    }

    @Test
    public void testAddComment() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM comments");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getFetchSize());
        int comment_id = pd.addDummyComment(users[0].getId(), blog_ids[0]);
        PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM comments WHERE id = ?");
        ps1.setInt(1, comment_id);
        ResultSet rs1 = ps1.executeQuery();
        rs1.next();
        int commID = rs1.getInt("id");
        int userID = rs1.getInt(2);
        int blog_id = rs1.getInt(3);
        assertEquals(comment_id, commID);
        assertEquals(blog_ids[0], blog_id);
        int realUserID = users[0].getId();
        assertEquals(realUserID, userID);

    }

    @Test
    public void testAddCommentCyclic() throws SQLException {

        for(int i = 0; i < users.length; i++){
            int comment_id = pd.addDummyComment(users[i].getId(), blog_ids[1]);
            PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM comments WHERE id = ?");
            ps1.setInt(1, comment_id);
            ResultSet rs1 = ps1.executeQuery();
            rs1.next();
            int commID = rs1.getInt(1);
            int userID = rs1.getInt(2);
            int blog_id = rs1.getInt(3);
            assertEquals(comment_id, commID);
            assertEquals(blog_ids[1], blog_id);
            int realUserID = users[i].getId();
            assertEquals(realUserID, userID);
        }
    }

    @Test
    public void testEditCommentCyclic() throws SQLException {
        int [] comment_ids = new int[NUM_USERS];
        for(int i = 0; i < users.length; i++){
            int comment_id = pd.addDummyComment(users[i].getId(), blog_ids[2]);
            comment_ids[i] = comment_id;
        }

        for(int i = 0; i < users.length; i++){
            commentDAO.editComment(comment_ids[i], "Forza Ferrari");
            PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM comments WHERE id = ?");
            ps1.setInt(1, comment_ids[i]);
            ResultSet rs1 = ps1.executeQuery();
            rs1.next();
            String comment = rs1.getString(4);
            assertEquals("Forza Ferrari", comment);
        }
    }

    @Test
    public void testDeleteComment() throws SQLException {
        int [] comment_ids = new int[NUM_USERS];
        for(int i = 0; i < users.length; i++){
            int comment_id = pd.addDummyComment(users[i].getId(), blog_ids[3]);
            comment_ids[i] = comment_id;
            PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM comments WHERE id = ?");
            ps1.setInt(1, comment_id);
            ResultSet rs1 = ps1.executeQuery();
            rs1.next();
            int commID = rs1.getInt(1);
            int userID = rs1.getInt(2);
            int blog_id = rs1.getInt(3);
            assertEquals(comment_id, commID);
            assertEquals(blog_ids[3], blog_id);
            int realUserID = users[i].getId();
            assertEquals(realUserID, userID);
        }

        for(int i = 0; i < users.length; i++){
            PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM comments WHERE id = ?");
            ps1.setInt(1, comment_ids[i]);
            ResultSet rs1 = ps1.executeQuery();
            rs1.next();
            assertFalse(rs1.next());
        }
    }

    @Test
    public void testGetComments() throws SQLException {
        int [] comment_ids = new int[NUM_USERS];
        for(int i = 0; i < users.length; i++){
            int comment_id = pd.addDummyComment(users[i].getId(), blog_ids[4]);
            comment_ids[i] = comment_id;
        }

        List<Comment> blogComments = commentDAO.getComments(blog_ids[4]);

        for(int i = 0; i < blogComments.size(); i++){
            assertEquals(comment_ids[i], blogComments.get(i).getComment_id());
        }

    }

    @Test
    public void testGetCommentsCyclic() throws SQLException {

        for(int j = 5; j <= 10; j++) {
            int[] comment_ids = new int[NUM_USERS];
            for (int i = 0; i < users.length; i++) {
                int comment_id = pd.addDummyComment(users[i].getId(), blog_ids[j]);
                comment_ids[i] = comment_id;
            }

            List<Comment> blogComments = commentDAO.getComments(blog_ids[j]);

            for (int i = 0; i < blogComments.size(); i++) {
                assertEquals(comment_ids[i], blogComments.get(i).getComment_id());
            }
        }
    }

    @Test
    public void testLikeComment() throws SQLException {
        int comment_id = pd.addDummyComment(users[0].getId(), blog_ids[0]);
        for(int i = 0; i < users.length; i++) {
            commentDAO.likeComment(comment_id, users[i].getId());
            PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM likes WHERE comment_id = ? and user_id = ?");
            ps1.setInt(1, comment_id);
            ps1.setInt(2, users[i].getId());
            ResultSet rs = ps1.executeQuery();
            assertTrue(rs.next());
        }
    }

    @Test
    public void testLikeCommentCyclic() throws SQLException{

        for(int j = 0; j < users.length; j++) {
            int comment_id = pd.addDummyComment(users[j].getId(), blog_ids[0]);
            for (int i = 0; i < users.length; i++) {
                commentDAO.likeComment(comment_id, users[i].getId());
                PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM likes WHERE comment_id = ? and user_id = ?");
                ps1.setInt(1, comment_id);
                ps1.setInt(2, users[i].getId());
                ResultSet rs = ps1.executeQuery();
                assertTrue(rs.next());
            }
            List<User> commentLikers = commentDAO.getCommentLikeUsers(comment_id);
            assertEquals(users.length, commentDAO.getNumberOfLikes(comment_id));
            for(int i = 0; i < commentLikers.size(); i++){
                assertEquals(users[i].getNickname(), commentLikers.get(i).getNickname());
                assertEquals(users[i].getFirstname(), commentLikers.get(i).getFirstname());
                assertEquals(users[i].getLastname(), commentLikers.get(i).getLastname());
            }
        }
    }

    @Test
    public void testUnlikeCommentCyclic() throws SQLException{
        int comment_id = pd.addDummyComment(users[0].getId(), blog_ids[0]);

        for (int i = 0; i < users.length; i++) {
          commentDAO.likeComment(comment_id, users[i].getId());
        }

        for(int j = 0; j < users.length; j++) {
            commentDAO.unlikeComment(comment_id, users[j].getId());
        }

        for(int j = 0; j < users.length; j++) {
            for (int i = 0; i < users.length; i++) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM likes WHERE user_id = ? AND comment_id = ?");
                ps.setInt(1, users[i].getId());
                ps.setInt(2, comment_id);
                ResultSet rs = ps.executeQuery();
                assertFalse(rs.next());
            }
        }
    }






}
