package org.blogstagram.Validators;

import org.blogstagram.dao.CommentDAO;

import java.sql.SQLException;

public class BlogExistsValidator implements CommentSystemValidator{

    CommentDAO commentDAO;

    public void setCommentDAO(CommentDAO commentDAO){

        this.commentDAO = commentDAO;
    }

    @Override
    public boolean validate(Object obj) throws SQLException {
        int blog_id = Integer.parseInt((String)obj);
        return commentDAO.blogExists(blog_id);
    }


}
