package org.blogstagram.blogs.edit;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;

import java.util.List;

public interface EditBlog {
    void editTitle(int BlogId, String title) throws DatabaseError, InvalidSQLQueryException;
    void editContent(int blogId, String content) throws InvalidSQLQueryException, DatabaseError;
    void editModerators(Blog blogId, List<User> newModerators) throws DatabaseError, InvalidSQLQueryException;
}
