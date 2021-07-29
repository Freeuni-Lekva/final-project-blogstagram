package org.blogstagram.validators;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RemoveRequestValidator implements Validator{

    private final Integer blogId;
    private final Integer currentUserId;
    private SqlBlogDAO blogDAO;
    private UserDAO userDAO;
    private List <GeneralError> errors;

    public void setBlogDAO(SqlBlogDAO blogDAO) {
        if(blogDAO == null)
        this.blogDAO = blogDAO;
        errors = new ArrayList<>();
    }

    public RemoveRequestValidator(Integer blogId, Integer currentUserId){
        this.blogId = blogId;
        this.currentUserId = currentUserId;
    }

    @Override
    public boolean validate() throws SQLException {
        try {
            Blog currentBlog = blogDAO.getBlog(blogId);
            User currentUser = userDAO.getUserByID(currentUserId);
            if(currentBlog.getUser_id() == currentUser.getId() || currentUser.getRole().equals(User.MODERATOR_ROLE) ||
                    currentUser.getRole().equals(User.ADMIN_ROLE)) return true;
            errors.add(new VariableError("user id", "user who removes this blog must be admin, moderator or user who created this blog."));
            return false;
        } catch (InvalidSQLQueryException | DatabaseError  e) {
            e.printStackTrace();
            errors.add(new VariableError("blogId", "Blog with this id must be already added"));
            return false;
        }
    }

    @Override
    public List<GeneralError> getErrors() {
        return null;
    }
}
