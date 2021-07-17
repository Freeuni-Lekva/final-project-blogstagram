package org.blogstagram.Validators;

import org.blogstagram.dao.CommentDAO;

import java.sql.SQLException;

public class CommentAddValidator implements CommentSystemValidator{
    CommentDAO commentDAO;

    @Override
    public boolean validate(Object obj) throws SQLException {
        String comment = (String) obj;
        if(comment.length() > 600 || comment.length() == 0){
            return false;
        }
        return true;
    }

    public void setCommentDAO(CommentDAO commentDAO) {

        this.commentDAO = commentDAO;
    }
}
