package org.blogstagram.validators;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.*;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RemoveRequestValidator implements Validator{

    private final Integer blogId;
    private final Integer currentUserId;
    private final SqlBlogDAO blogDAO;
    private final UserDAO userDAO;
    private List <GeneralError> errors;


    public RemoveRequestValidator(Integer blogId, Integer currentUserId, SqlBlogDAO blogDAO, UserDAO userDAO){
        this.blogId = blogId;
        this.currentUserId = currentUserId;
        this.blogDAO = blogDAO;
        this.userDAO = userDAO;
        this.errors = new ArrayList<>();
    }

    @Override
    public boolean validate() throws SQLException {
        try {
            if(!blogDAO.blogExists(blogId)) {
                errors.add(new VariableError("blog id", "blog id must exist"));
                return false;
            }
            Blog currentBlog = blogDAO.getBlog(blogId);
            User currentUser = userDAO.getUserByID(currentUserId);
            if(currentUser == null){
                errors.add(new VariableError("user id", "user must be registered"));
                return false;
            }
            if(currentBlog.getUser_id() == currentUser.getId() || currentUser.getRole().equals(User.MODERATOR_ROLE) ||
                    currentUser.getRole().equals(User.ADMIN_ROLE)) return true;
            errors.add(new VariableError("user id", "user who removes this blog must be admin, moderator or user who created this blog."));
            return false;
        } catch (InvalidSQLQueryException | DatabaseError e) {
            e.printStackTrace();
            errors.add(new VariableError("blogId", e.getMessage()));
            return false;
        }
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
