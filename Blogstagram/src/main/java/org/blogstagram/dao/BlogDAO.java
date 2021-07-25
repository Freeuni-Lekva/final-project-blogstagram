package org.blogstagram.dao;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;

import java.util.List;

public interface BlogDAO {
    void addBlog(Blog newBlog) throws InvalidSQLQueryException, DatabaseError; // insert
    void removeBlog(Blog blog) throws InvalidSQLQueryException, DatabaseError; // delete
    List<User> getModerators(int blogId) throws InvalidSQLQueryException, DatabaseError; // select
    void removeModerators(int blogId, List <User> moderators) throws InvalidSQLQueryException;
    void addModerators(int blogId, List <User> moderators) throws InvalidSQLQueryException, DatabaseError;
    void editBlog(Blog blog) throws InvalidSQLQueryException, DatabaseError; // update
    List <Blog> getBlogsOfUser(int id) throws DatabaseError, InvalidSQLQueryException; // select
    Blog getBlog(int id) throws InvalidSQLQueryException; //select
    boolean blogExists(int id) throws InvalidSQLQueryException; //select
    int getAmountOfBlogsByUser(int id) throws DatabaseError, InvalidSQLQueryException;
}
