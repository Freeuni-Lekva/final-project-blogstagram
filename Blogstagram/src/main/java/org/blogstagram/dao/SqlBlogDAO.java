package org.blogstagram.dao;

import org.blogstagram.blogs.edit.*;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.HashTag;
import org.blogstagram.models.User;
import org.blogstagram.sql.BlogQueries;
import org.blogstagram.sql.SqlQueries;
import org.blogstagram.validators.UserIdValidator;

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
    private final CommentDAO commentDAO;
    private final UserDAO userDAO;



    public SqlBlogDAO(Connection connection, UserDAO userDAO, int usePurpose, CommentDAO commentDAO){
        if(connection == null)
            throw new NullPointerException("Connection object can't be null.");
        else if(usePurpose != TEST && usePurpose != REAL){
            throw new IllegalArgumentException("Use purpose must be Test or real");
        }

        this.connection = connection;
        blogQueries = new BlogQueries(usePurpose);
        this.userDAO = userDAO;
        this.commentDAO = commentDAO;
        moderatorDao = new SqlBlogModeratorDao(connection);
        moderatorDao.setUserDao(userDAO);
        hashTagDao = new SqlHashTagDao(connection);
        editable = new ArrayList<>();
        editable.add(new EditTitle(this));
        editable.add(new EditContent(this));
        editable.add(new EditModerators(this));
        editable.add(new EditHashtags(this));
    }


    /*
        addes new blog in database
     */

    @Override
    public void addBlog(Blog newBlog) throws InvalidSQLQueryException, DatabaseError {
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

    /*
        removes blog from database
     */
    @Override
    public void removeBlog(Blog blog) throws InvalidSQLQueryException, DatabaseError {
        String removeBlogQuery = blogQueries.getDeleteQuery(Collections.singletonList("id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(removeBlogQuery);
            prpStm.setInt(1, blog.getId());
            int affectedRows = prpStm.executeUpdate();
            assertEquals(affectedRows, 1);
        } catch (SQLException exception) {
            throw new DatabaseError("Can't connect to database");
        }
    }


    /*
        returns list of moderators, users whitch have permision to change blog.
     */
    @Override
    public List <User> getModerators(int blogId) throws InvalidSQLQueryException, DatabaseError {
        return moderatorDao.getModerators(blogId);
    }


    /*
        takes list of users, who is moderator of current blogId and deletes them from moderators table.
     */
    @Override
    public void removeModerators(int blogId, List<User> moderators) throws InvalidSQLQueryException {
        moderatorDao.removeModerators(blogId, moderators);
    }

    /*
        add's moderators in the moderators table, so users can change blog info.
     */
    @Override
    public void addModerators(int blogId, List<User> moderators) throws InvalidSQLQueryException, DatabaseError {
        moderatorDao.addModerators(blogId, moderators);
    }

    /*
        Function for changing blog Info, it checks if current blog info changed, and if this condition is true then changes it.
     */
    @Override
    public void editBlog(Blog blog) throws InvalidSQLQueryException, DatabaseError {
        Blog currentBlog = getBlog(blog.getId());
        int current = 0;
        for(; current < editable.size(); current++){
            Edit edit = editable.get(current);
            if(edit.mustEdit(blog, currentBlog)){
                edit.edit(blog, currentBlog);
            }
        }
    }

    /*
        private function for intializing blog object.
     */
    private void initBlogObject(Blog blog, ResultSet resultSet) throws SQLException, DatabaseError, InvalidSQLQueryException {
        blog.setId(resultSet.getInt(1));
        blog.setUser_id(resultSet.getInt(2));
        blog.setTitle(resultSet.getString(3));
        blog.setContent(resultSet.getString(4));
        blog.setCreated_at(resultSet.getDate(5));
        blog.setBlogModerators(moderatorDao.getModerators(blog.getId()));
        blog.setHashTagList(hashTagDao.getHashTags(blog.getId()));
        blog.setComments(commentDAO.getComments(blog.getId()));
        blog.setNumLikes(0);
    }

    /*
        returns blogs which was created from user whose id is user_id
     */
    @Override
    public List <Blog> getBlogsOfUser(int user_id) throws DatabaseError, InvalidSQLQueryException {
        String query = blogQueries.getSelectQuery(Arrays.asList("id", "user_id", "title", "content", "created_at"),
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

    /*
        returns blog with id blog id.
     */
    @Override
    public Blog getBlog(int id) throws InvalidSQLQueryException, DatabaseError {
        Blog blog = new Blog();
        String query = blogQueries.getSelectQuery(Arrays.asList("id", "user_id", "title", "content", "created_at"),
                Collections.singletonList("id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, id);
            ResultSet resultSet = prpStm.executeQuery();
            if(!resultSet.next()) return null;
            initBlogObject(blog, resultSet);
            return blog;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /*
        checks if blog with id blogID exists
     */
    @Override
    public boolean blogExists(int blogId) throws InvalidSQLQueryException, DatabaseError {
        return getBlog(blogId) != null;
    }

    /*
        returns amount of blogs which was created by user who has given id.
     */
    @Override
    public int getAmountOfBlogsByUser(int id) throws DatabaseError, InvalidSQLQueryException {
        return getBlogsOfUser(id).size();
    }

    /*
        adds hashtags hooked on this blog.
     */
    @Override
    public void addHashtags(int blogId, List <HashTag> hashTags) throws DatabaseError, InvalidSQLQueryException {
        hashTagDao.addHashTags(blogId, hashTags);
    }

    /*
        removes hashtags from hashtags table.
     */
    @Override
    public void removeHashtags(int blogId, List<HashTag> hashTags) throws DatabaseError, InvalidSQLQueryException {
        hashTagDao.removeHashTags(blogId, hashTags);
    }

    // EditBlog interface



    /*
        Function updates title of current Blog.
     */
    @Override
    public void editTitle(int blogId, String newTitle) throws DatabaseError, InvalidSQLQueryException {
        String query = blogQueries.getUpdateQuery(Collections.singletonList("title"), Collections.singletonList("id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setString(1, newTitle);
            prpStm.setInt(2, blogId);
            System.out.println(prpStm);
            int affectedRows = prpStm.executeUpdate();
            assertEquals(affectedRows, 1);

        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to database");
        }
    }

    /*
        Function updates Content of current blog.
     */
    @Override
    public void editContent(int blogId, String newContent) throws InvalidSQLQueryException, DatabaseError {
        String query = blogQueries.getUpdateQuery(Collections.singletonList("content"), Collections.singletonList("id"));
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setString(1, newContent);
            prpStm.setInt(2, blogId);
            int affectedRows = prpStm.executeUpdate();
            assertEquals(affectedRows, 1);
        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to database");
        }
    }

    /*
        Function edits list of moderators.
     */
    @Override
    public void editModerators(Blog blog, List<User> newModerators) throws DatabaseError, InvalidSQLQueryException {
        moderatorDao.editModerators(blog, newModerators);
    }

    /*
        Function edits list of hashtags.
     */
    @Override
    public void editHashTags(Blog blog, List<HashTag> newHashTags) throws DatabaseError, InvalidSQLQueryException {
        hashTagDao.editHashTags(blog, newHashTags);
    }

}