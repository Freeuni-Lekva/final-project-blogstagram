package org.blogstagram.Validators;

import java.sql.SQLException;

public interface Validator {
    boolean validate(Object obj) throws SQLException;
}
