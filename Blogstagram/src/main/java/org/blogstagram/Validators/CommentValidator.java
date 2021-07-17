package org.blogstagram.Validators;

import org.blogstagram.dao.CommentDAO;

import java.sql.SQLException;

public class CommentValidator implements Validator{

    CommentDAO commentDAO;

    @Override
    public boolean validate(Object obj) throws SQLException {
        String comment = (String) obj;
        if(comment.length() > 600 || comment.length() == 0){
            return false;
        }
        return true;
    }

    public boolean commentDeleteValidator(Object obj) throws SQLException {
        int comment_id = (int)obj;
        return commentDAO.commentExists(comment_id);
    }

    public void setCommentDAO(CommentDAO commentDAO) {

        this.commentDAO = commentDAO;
    }
}
