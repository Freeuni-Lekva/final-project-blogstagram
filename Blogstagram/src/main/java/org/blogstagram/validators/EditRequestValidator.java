package org.blogstagram.validators;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.*;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditRequestValidator implements Validator {
    private final Integer currentUserId;
    private final Integer blogId;
    private final SqlBlogDAO blogDao;
    private final List <GeneralError> errors;

    public EditRequestValidator(Integer currentUserId, int blogId, SqlBlogDAO blogDAO) {
        this.currentUserId = currentUserId;
        this.blogId = blogId;
        this.blogDao = blogDAO;
        errors = new ArrayList<>();
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
            errors.add(new VariableError("user id", "user id must be one of moderators, or author of this blog"));
            return false;
        } catch (InvalidSQLQueryException | DatabaseError e) {
            errors.add(new VariableError("Blog", e.getMessage()));
            return false;
        }
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
