package org.blogstagram.dao;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;
import org.blogstagram.sql.BlogQueries;
import org.blogstagram.sql.SqlQueries;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SqlBlogDAO implements BlogDAO{
    public static final int TEST = 0;
    public static final int REAL = 1;
    private final SqlQueries blogQueries;
    private final BlogModeratorDao moderatorDao;
    private final Connection connection;





    public SqlBlogDAO(Connection connection, int usePurpose){
        if(connection == null)
            throw new NullPointerException("Connection object can't be null.");
        else if(usePurpose == TEST || usePurpose == REAL){
            throw new IllegalArgumentException("Use purpose must be Test or real");
        }

        this.connection = connection;
        blogQueries = new BlogQueries(usePurpose);
        moderatorDao = new SqlBlogModeratorDao(connection);
    }


    @Override
    public void addBlog(Blog newBlog) throws InvalidSQLQueryException, DatabaseError {
        // implement validating logic

        String addBlogQuery = blogQueries.getInsertQuery(Arrays.asList("user_id", "title", "content"), 1);
        try {
            PreparedStatement prpStm = connection.prepareStatement(addBlogQuery, Statement.RETURN_GENERATED_KEYS);
            prpStm.setInt(1, newBlog.getUser_id());
            prpStm.setString(2, newBlog.getTitle());
            prpStm.setString(3, newBlog.getContent());
            int affectedRows = prpStm.executeUpdate();
            ResultSet keys = prpStm.getGeneratedKeys();
            assertEquals(1, affectedRows);
            assertTrue(keys.next());
            newBlog.setId(keys.getInt(1));
            moderatorDao.addModerators(newBlog.getId(), newBlog.getBlogModerators());
        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to Database");
        }

    }

    @Override
    public void removeBlog(Blog blog) {
        //String query =
    }

    @Override
    public List<User> getModerators(int blogId) throws InvalidSQLQueryException, DatabaseError {
        return moderatorDao.getModerators(blogId);
    }

    @Override
    public void removeModerators(int blogId, List<User> moderators) throws InvalidSQLQueryException {
        moderatorDao.removeModerators(blogId, moderators);
    }


    @Override
    public void addModerators(int blogId, List<User> moderators) throws InvalidSQLQueryException, DatabaseError {
        moderatorDao.addModerators(blogId, moderators);
    }


    @Override
    public void editBlog(Blog blog) {
        // implement
    }

    @Override
    public List<Blog> getBlogsOfUser(int user_id) throws DatabaseError, InvalidSQLQueryException {
        String query = blogQueries.getSelectQuery(Arrays.asList("id", "user_id", "title", "content", "created_At"),
                Collections.singletonList("user_id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, user_id);
            List <Blog> blogs = new ArrayList<>();
            ResultSet resultSet = prpStm.executeQuery();
            while(resultSet.next()){
                Blog blog = new Blog();
                blog.setId(resultSet.getInt(1));
                blog.setUser_id(resultSet.getInt(2));
                blog.setTitle(resultSet.getString(3));
                blog.setContent(resultSet.getString(4));
                blog.setCreated_at(resultSet.getDate(5));
                blogs.add(blog);
            }
            return blogs;
        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to database.");
        }
    }

    @Override
    public Blog getBlog(int id) throws InvalidSQLQueryException {
        Blog blog = new Blog();
        String query = blogQueries.getSelectQuery(Arrays.asList("id", "user_id", "title", "content", "created_at"),
                Collections.singletonList("id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, id);
            ResultSet resultSet = prpStm.executeQuery();
            assertTrue(resultSet.next());
            blog.setId(resultSet.getInt(1));
            blog.setUser_id(resultSet.getInt(2));
            blog.setTitle(resultSet.getString(3));
            blog.setContent(resultSet.getString(4));
            blog.setCreated_at(resultSet.getDate(5));
            blog.setBlogModerators(getModerators(blog.getId()));
            return blog;
        } catch (SQLException | DatabaseError exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean blogExists(int id) throws InvalidSQLQueryException {
        return getBlog(id) != null;
    }

    @Override
    public int getAmountOfBlogsByUser(int id) throws DatabaseError, InvalidSQLQueryException {
        return getBlogsOfUser(id).size();
    }
}
