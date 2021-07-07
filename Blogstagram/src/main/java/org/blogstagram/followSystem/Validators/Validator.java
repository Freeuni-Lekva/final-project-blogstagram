package org.blogstagram.followSystem.Validators;

import org.blogstagram.errors.NotValidUserIdException;

public interface Validator {
    boolean validate(Object obj) throws NotValidUserIdException;
}
