package org.blogstagram.validators;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.models.Blog;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlogValidator implements Validator {
    private final Blog currentBlog;
    private final List <GeneralError> errors;
    private final SqlBlogDAO blogDAO;



    public BlogValidator(Blog blog, SqlBlogDAO blogDAO){
        if(blog == null) throw new NullPointerException("blog object can't be null.");
        if(blogDAO == null) throw new NullPointerException("BlogDao object can't be null.");
        currentBlog = blog;
        errors = new ArrayList<>();
        this.blogDAO = blogDAO;
    }

    @Override
    public boolean validate() throws SQLException {
        List <Validator> validators = Arrays.asList(new BlogIdValidator(currentBlog.getId(), blogDAO), new BlogTitleValidator(currentBlog.getTitle()), new BlogContentValidator(currentBlog.getContent()),
                new BlogModeratorsValidator(currentBlog.getBlogModerators()));
        boolean result = true;
        for(Validator validator : validators){
            result = result && validator.validate();
            errors.addAll(validator.getErrors());
        }
        return result;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
