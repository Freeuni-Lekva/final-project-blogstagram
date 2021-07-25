package org.blogstagram.validators;

import java.sql.SQLException;

public interface CommentSystemValidator {
    boolean validate(Object obj1, Object obj2) throws SQLException;
}
