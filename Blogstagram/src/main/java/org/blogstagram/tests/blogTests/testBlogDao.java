package org.blogstagram.tests.blogTests;

import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.dao.CommentDAO;
import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.HashTag;
import org.blogstagram.models.User;
import org.blogstagram.sql.BlogQueries;
import org.blogstagram.sql.SqlQueries;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class testBlogDao {
    private static BasicDataSource source;
    private static Connection conn;
    private static SqlBlogDAO sqlBlogDAO;
    private static SqlQueries queries;
    private static UserDAO userDAO;

    @BeforeClass
    public static void init(){
        source = new BasicDataSource();
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb_test");
        source.setUsername("root");
        source.setPassword("Arqimede123@");
        try {
            conn = source.getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        userDAO = new UserDAO(conn);
        CommentDAO commentDAO = new CommentDAO(conn);
        sqlBlogDAO = new SqlBlogDAO(conn, userDAO, SqlBlogDAO.REAL, commentDAO);
        queries = new BlogQueries(SqlBlogDAO.REAL);
        setUpUsers();
    }

    private static void setUpUsers() {
        for(int k = 0; k < 10; k++){
            Date birthday = new java.sql.Date(System.currentTimeMillis());
            User user = new User(null,"firstName" + k, "lastName" + k, "nickname" + k, User.DEFAULT_ROLE,
                    "email" + k + "@gmail.com", User.FEMALE, User.PUBLIC, birthday , User.DEFAULT_USER_IMAGE_PATH,
                    "country" + k,"city" + k, "", "", new Date());
            try {
                userDAO.addUser(user, "password1");
            } catch (SQLException exception) {
                return;
            }
        }
    }


    private void deleteFromDataBase(int id){
        List<String> where = Collections.singletonList("id");
        try {
            String query = queries.getDeleteQuery(where);
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setInt(1, id);
            int affected = stm.executeUpdate();
            assertEquals(affected, 1);
        } catch (InvalidSQLQueryException | SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testAddGet(){
        Blog newBlog = new Blog();
        newBlog.setUser_id(1);
        newBlog.setTitle("title1");
        newBlog.setContent("content2");
        newBlog.setBlogModerators(Collections.<User>emptyList());
        newBlog.setHashTagList(Arrays.asList(new HashTag("cool"), new HashTag("coolest"), new HashTag("cool cool")));
        try {
            sqlBlogDAO.addBlog(newBlog);
            assertNotEquals(newBlog.getId(), Blog.NO_BLOG_ID);
            Blog actual = sqlBlogDAO.getBlog(newBlog.getId());
            assertEquals(newBlog, actual);
            deleteFromDataBase(newBlog.getId());
        } catch (InvalidSQLQueryException | DatabaseError e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddGetMultiple(){
        Blog newBlog = new Blog();
        newBlog.setUser_id(1);
        newBlog.setTitle("title1");
        newBlog.setContent("content2");
        newBlog.setBlogModerators(Collections.<User>emptyList());
        newBlog.setHashTagList(Arrays.asList(new HashTag("cool"), new HashTag("coolest"), new HashTag("cool cool")));
        List <Integer> ids = new ArrayList<>();
        try {
            for(int k = 0; k < 20; k++){
                sqlBlogDAO.addBlog(newBlog);
                assertNotEquals(newBlog.getId(), Blog.NO_BLOG_ID);
                Blog actual = sqlBlogDAO.getBlog(newBlog.getId());
                assertTrue(actual.equals(newBlog));
                ids.add(actual.getId());
            }
            for(Integer id : ids){
                deleteFromDataBase(id);
            }
        } catch (InvalidSQLQueryException | DatabaseError e){
            e.printStackTrace();
        }
    }


    @Test
    public void testRemoveBlog(){
        Blog newBlog = new Blog();
        newBlog.setUser_id(1);
        newBlog.setTitle("title1");
        newBlog.setContent("content2");
        newBlog.setBlogModerators(Collections.<User>emptyList());
        newBlog.setHashTagList(Arrays.asList(new HashTag("cool"), new HashTag("coolest"), new HashTag("cool cool")));
        try {
            sqlBlogDAO.addBlog(newBlog);
            assertNotEquals(newBlog.getId(), -1);
            sqlBlogDAO.removeBlog(newBlog);
            assertNull(sqlBlogDAO.getBlog(newBlog.getId()));
        } catch (InvalidSQLQueryException | DatabaseError e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testRemoveBlogMultiple(){
        Blog newBlog = new Blog();
        newBlog.setUser_id(1);
        newBlog.setTitle("title1");
        newBlog.setContent("content2");
        newBlog.setBlogModerators(Collections.<User>emptyList());
        newBlog.setHashTagList(Arrays.asList(new HashTag("cool"), new HashTag("coolest"), new HashTag("cool cool")));
        for(int k = 0; k < 10; k++){
            try {
                sqlBlogDAO.addBlog(newBlog);
                assertNotEquals(newBlog.getId(), -1);
                sqlBlogDAO.removeBlog(newBlog);
                assertNull(sqlBlogDAO.getBlog(newBlog.getId()));
            } catch (InvalidSQLQueryException | DatabaseError e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetBlogsOfUser(){
        int creatorId = 1;
        ArrayList <Integer> ids = new ArrayList<>();
        for(int k = 0; k < 20; k++){
            Blog blog = new Blog();
            blog.setUser_id(creatorId);
            blog.setTitle("title" + k);
            blog.setContent("content" + k);
            blog.setHashTagList(Arrays.asList(new HashTag("1"), new HashTag("2"), new HashTag("3")));
            try {
                blog.setBlogModerators(Arrays.asList(userDAO.getUserByID(1), userDAO.getUserByID(2), userDAO.getUserByID(3)));
                blog.setNumLikes(k);
                sqlBlogDAO.addBlog(blog);
                assertNotEquals(blog.getId(), -1);
                ids.add(blog.getId());
            } catch (SQLException | DatabaseError | InvalidSQLQueryException exception) {
                exception.printStackTrace();
            }
        }

        try {
            List <Blog> blogs = sqlBlogDAO.getBlogsOfUser(creatorId);
            assertEquals(blogs.size(), ids.size());
            int ind = 0;
            for(Integer id : ids){
                assertEquals(id, (Integer) blogs.get(ind).getId());
                sqlBlogDAO.removeBlog(blogs.get(ind));
                ind++;
            }

        } catch (DatabaseError | InvalidSQLQueryException databaseError) {
            databaseError.printStackTrace();
        }


    }

    @Test
    public void testGetNumBlogs(){
        int creatorId = 1;
        ArrayList <Integer> ids = new ArrayList<>();
        for(int k = 0; k < 20; k++){
            Blog blog = new Blog();
            blog.setUser_id(creatorId);
            blog.setTitle("title" + k);
            blog.setContent("content" + k);
            blog.setHashTagList(Arrays.asList(new HashTag("1"), new HashTag("2"), new HashTag("3")));
            try {
                blog.setBlogModerators(Arrays.asList(userDAO.getUserByID(1), userDAO.getUserByID(2), userDAO.getUserByID(3)));
                blog.setNumLikes(k);
                sqlBlogDAO.addBlog(blog);
                assertNotEquals(blog.getId(), -1);
                ids.add(blog.getId());
            } catch (SQLException | DatabaseError | InvalidSQLQueryException exception) {
                exception.printStackTrace();
            }
        }

        try {
            List <Blog> blogs = sqlBlogDAO.getBlogsOfUser(creatorId);
            assertEquals(blogs.size(), ids.size());
            try {
                assertEquals(sqlBlogDAO.getAmountOfBlogsByUser(creatorId), ids.size());
            } catch (DatabaseError | InvalidSQLQueryException databaseError) {
                databaseError.printStackTrace();
            }
            int ind = 0;
            for(Integer id : ids){
                assertEquals(id, (Integer) blogs.get(ind).getId());
                sqlBlogDAO.removeBlog(blogs.get(ind));
                ind++;
            }
            assertEquals(0, sqlBlogDAO.getAmountOfBlogsByUser(creatorId));
        } catch (DatabaseError | InvalidSQLQueryException databaseError) {
            databaseError.printStackTrace();
        }

    }



}
