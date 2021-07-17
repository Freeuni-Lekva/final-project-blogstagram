package org.blogstagram.Validators;

import java.sql.SQLException;

public interface CommentSystemValidator {
    boolean validate(Object obj) throws SQLException;
}
