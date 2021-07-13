package org.blogstagram.Validators;

import org.blogstagram.dao.CommentDAO;

import java.sql.SQLException;

public class CommentLikeValidator{
    CommentDAO commentDAO;

    public void setCommentDAO(CommentDAO commentDAO){

        this.commentDAO = commentDAO;
    }

    public boolean validate(Object obj1, Object obj2) throws SQLException {
        int comment_id = Integer.parseInt((String)obj1);
        int user_id = Integer.parseInt((String)obj2);
        return commentDAO.commentIsLiked(comment_id, user_id);
    }
}
