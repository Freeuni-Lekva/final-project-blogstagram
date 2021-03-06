package org.blogstagram.validators;

import org.blogstagram.dao.CommentDAO;

import java.sql.SQLException;

public class CommentAddValidator implements CommentSystemValidator{
    @Override
    public boolean validate(Object obj, Object obj1) throws SQLException {
        String comment = (String) obj;
        return comment.length() <= 600 && comment.length() != 0;
    }

}
