package org.blogstagram.blogs.edit;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;

public interface Edit {
    boolean mustEdit(Blog changeBlog, Blog currentBlog);
    void edit(Blog changeBlog, Blog currentBlog) throws DatabaseError, InvalidSQLQueryException;
}
