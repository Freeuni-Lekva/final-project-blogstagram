package org.blogstagram.dao;

import org.blogstagram.blogs.edit.*;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.HashTag;
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

public class SqlBlogDAO implements BlogDAO, EditBlog {
    public static final int TEST = 0;
    public static final int REAL = 1;
    private final SqlQueries blogQueries;
    private final SqlBlogModeratorDao moderatorDao;
    private final Connection connection;
    private final List <Edit> editable;
    private final SqlHashTagDao hashTagDao;
    private UserDAO userDAO;



    public SqlBlogDAO(Connection connection, UserDAO userDAO, int usePurpose){
        if(connection == null)
            throw new NullPointerException("Connection object can't be null.");
        else if(usePurpose != TEST && usePurpose != REAL){
            throw new IllegalArgumentException("Use purpose must be Test or real");
        }

        this.connection = connection;
        blogQueries = new BlogQueries(usePurpose);
        this.userDAO = userDAO;
        moderatorDao = new SqlBlogModeratorDao(connection);
        moderatorDao.setUserDao(userDAO);
        hashTagDao = new SqlHashTagDao(connection);
        editable = Arrays.asList(new EditTitle(this), new EditContent(this),
                new EditModerators(this), new EditHashtags(this));
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
            addModerators(newBlog.getId(), newBlog.getBlogModerators());
            addHashtags(newBlog.getId(), newBlog.getHashTagList());
        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to Database");
        }

    }

    @Override
    public void removeBlog(Blog blog) throws InvalidSQLQueryException, DatabaseError {
        String removeBlogQuery = blogQueries.getDeleteQuery(Collections.singletonList("blog_id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(removeBlogQuery);
            prpStm.setInt(1, blog.getId());
            int affectedRows = prpStm.executeUpdate();
            assertEquals(affectedRows, 1);
            removeModerators(blog.getId(), blog.getBlogModerators());
            removeHashtags(blog.getId(), blog.getHashTagList());
        } catch (SQLException exception) {
            throw new DatabaseError("Can't connect to database");
        }
    }

    @Override
    public List <User> getModerators(int blogId) throws InvalidSQLQueryException, DatabaseError {
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
    public void editBlog(Blog blog) throws InvalidSQLQueryException, DatabaseError {
        Blog currentBlog = getBlog(blog.getId());
        for(Edit edit : editable){
            if(edit.mustEdit(blog, currentBlog)) edit.edit(blog, currentBlog);
        }
    }

    private void initBlogObject(Blog blog, ResultSet resultSet) throws SQLException, DatabaseError, InvalidSQLQueryException {
        blog.setId(resultSet.getInt(1));
        blog.setUser_id(resultSet.getInt(2));
        blog.setTitle(resultSet.getString(3));
        blog.setContent(resultSet.getString(4));
        blog.setCreated_at(resultSet.getDate(5));
        blog.setBlogModerators(moderatorDao.getModerators(blog.getId()));
        blog.setHashTagList(hashTagDao.getHashTags(blog.getId()));
    }

    @Override
    public List <Blog> getBlogsOfUser(int user_id) throws DatabaseError, InvalidSQLQueryException {
        String query = blogQueries.getSelectQuery(Arrays.asList("id", "user_id", "title", "content", "created_At"),
                Collections.singletonList("user_id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, user_id);
            List <Blog> blogs = new ArrayList<>();
            ResultSet resultSet = prpStm.executeQuery();
            while(resultSet.next()){
                Blog blog = new Blog();
                initBlogObject(blog, resultSet);
                blogs.add(blog);
            }
            return blogs;
        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to database.");
        }
    }

    @Override
    public Blog getBlog(int id) throws InvalidSQLQueryException, DatabaseError {
        Blog blog = new Blog();
        String query = blogQueries.getSelectQuery(Arrays.asList("id", "user_id", "title", "content", "created_at"),
                Collections.singletonList("id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, id);
            ResultSet resultSet = prpStm.executeQuery();
            assertTrue(resultSet.next());
            initBlogObject(blog, resultSet);
            return blog;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean blogExists(int blogId) throws InvalidSQLQueryException, DatabaseError {
        return getBlog(blogId) != null;
    }

    @Override
    public int getAmountOfBlogsByUser(int id) throws DatabaseError, InvalidSQLQueryException {
        return getBlogsOfUser(id).size();
    }

    @Override
    public void addHashtags(int blogId, List <HashTag> hashTags) throws DatabaseError, InvalidSQLQueryException {
        hashTagDao.addHashTags(blogId, hashTags);
    }

    @Override
    public void removeHashtags(int blogId, List<HashTag> hashTags) throws DatabaseError, InvalidSQLQueryException {
        hashTagDao.removeHashTags(blogId, hashTags);
    }

    @Override
    public void editTitle(int blogId, String newTitle) throws DatabaseError, InvalidSQLQueryException {
        String query = blogQueries.getUpdateQuery(Collections.singletonList("title"), Collections.singletonList("blog_id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, blogId);
            prpStm.setString(2, newTitle);
            int affectedRows = prpStm.executeUpdate();
            assertEquals(affectedRows, 1);
        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to database");
        }
    }

    @Override
    public void editContent(int blogId, String newContent) throws InvalidSQLQueryException, DatabaseError {
        String query = blogQueries.getUpdateQuery(Collections.singletonList("content"), Collections.singletonList("blog_id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, blogId);
            prpStm.setString(2, newContent);
            int affectedRows = prpStm.executeUpdate();
            assertEquals(affectedRows, 1);
        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to database");
        }
    }

    @Override
    public void editModerators(Blog blog, List<User> newModerators) throws DatabaseError, InvalidSQLQueryException {
        moderatorDao.editModerators(blog, newModerators);
    }

    @Override
    public void editHashTags(Blog blog, List<HashTag> newHashTags) throws DatabaseError, InvalidSQLQueryException {
        hashTagDao.editHashTags(blog, newHashTags);
    }

}
