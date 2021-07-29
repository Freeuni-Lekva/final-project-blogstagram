package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlogContentValidator implements Validator{
    private static final int CONTENT_MIN_LENGTH = 50;
    private static final int CONTENT_MAX_LENGTH = 1000;
    private final String content;
    private final List <GeneralError> errors;

    public BlogContentValidator(String content){
        this.content = content;
        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() throws SQLException {
        if(content == null) {
            errors.add(new VariableError("content", "content can't be null"));
            return false;
        }
        if(content.length() >= CONTENT_MIN_LENGTH && content.length() <= CONTENT_MAX_LENGTH) {
            errors.add(new VariableError("content", "Content's length must be between " + "[" + CONTENT_MIN_LENGTH + ", " + CONTENT_MAX_LENGTH + "]"));
            return false;
        }
        return true;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
