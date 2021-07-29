package org.blogstagram.validators;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditRequestValidator implements Validator {
    private Integer currentUserId;
    private Integer blogId;
    private SqlBlogDAO blogDao;
    private List <GeneralError> errors;

    public EditRequestValidator(Integer currentUserId, int blogId) {
        this.currentUserId = currentUserId;
        this.blogId = blogId;
        errors = new ArrayList<>();
    }

    public void setBlogDao(SqlBlogDAO blogDao) {
        if(blogDao == null)
            errors.add(new VariableError("blogDao", "Blog dao object can't be null!"));
        this.blogDao = blogDao;
    }

    @Override
    public boolean validate() throws SQLException {
        if(errors.size() != 0) return false;
        try {
            Blog currentBlog = blogDao.getBlog(blogId);
            if(currentBlog.getUser_id() == currentUserId) return true;
            List <User> blogModerators = currentBlog.getBlogModerators();
            for (User blogModerator : blogModerators) {
                if (blogModerator.getId().equals(currentUserId)) return true;
            }
            errors.add(new VariableError("user id", "user id must be one of moderators, or author og this blog"));
            return false;
        } catch (InvalidSQLQueryException | DatabaseError e) {
            errors.add(new VariableError("Blog", "Blog id must be valid"));
            return false;
        }
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
