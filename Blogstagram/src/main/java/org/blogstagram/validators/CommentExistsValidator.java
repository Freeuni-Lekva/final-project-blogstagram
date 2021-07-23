package org.blogstagram.validators;

import org.blogstagram.dao.CommentDAO;

import java.sql.SQLException;

public class CommentExistsValidator implements CommentSystemValidator{
    CommentDAO commentDAO;

    public void setCommentDAO(CommentDAO commentDAO) {
        this.commentDAO = commentDAO;
    }

    @Override
    public boolean validate(Object obj) throws SQLException {
        int comment_id = (int)obj;
        return commentDAO.commentExists(comment_id);
    }
}
