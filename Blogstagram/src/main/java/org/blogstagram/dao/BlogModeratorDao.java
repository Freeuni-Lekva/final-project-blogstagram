package org.blogstagram.dao;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;

import java.util.List;

public interface BlogModeratorDao {
    List<User> getModerators(int blogId) throws InvalidSQLQueryException, DatabaseError;
    void addModerators(int blogId, List <User> moderators) throws InvalidSQLQueryException, DatabaseError;
    void removeModerators(int blogId, List <User> moderators) throws InvalidSQLQueryException;
    void editModerators(Blog blog, List <User> newModerators) throws InvalidSQLQueryException, DatabaseError;
}
