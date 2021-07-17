package org.blogstagram.Validators;

import org.blogstagram.dao.CommentDAO;

import java.sql.SQLException;

public class CommentDeleteValidator implements CommentSystemValidator{

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