package org.blogstagram.Validators;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.NotValidUserIdException;

public interface Validator {
    boolean validate(Object obj) throws NotValidUserIdException, DatabaseError;
}
