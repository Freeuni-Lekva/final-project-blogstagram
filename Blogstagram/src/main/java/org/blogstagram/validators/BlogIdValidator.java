package org.blogstagram.validators;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.errors.*;
import org.blogstagram.models.Blog;
import org.blogstagram.sql.BlogQueries;
import org.blogstagram.sql.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlogIdValidator implements Validator{
    private final Integer blogId;
    private final List <GeneralError> errors;
    private final SqlBlogDAO blogDAO;

    public BlogIdValidator(Integer blogId, SqlBlogDAO blogDAO) {
        this.blogId = blogId;
        this.blogDAO = blogDAO;
        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() throws SQLException {
        if(blogId == null) {
            errors.add(new VariableError("blogId", "blogId can't be null."));
            return false;
        }

        if(blogId == Blog.NO_BLOG_ID) return true;
        try {
            if(!blogDAO.blogExists(blogId)) {
                errors.add(new VariableError("blog id", "blog is must be real blog id which is already added"));
                return false;
            }
            return true;
        } catch (InvalidSQLQueryException | DatabaseError e) {
            errors.add(new VariableError("blog id", e.getMessage()));
            return false;
        }
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
