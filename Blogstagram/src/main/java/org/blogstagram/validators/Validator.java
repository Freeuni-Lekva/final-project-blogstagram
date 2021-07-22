package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;

import java.sql.SQLException;
import java.util.List;

public interface Validator {
    // Sets the errors
    public boolean validate() throws SQLException;

    public List<GeneralError> getErrors();
}
