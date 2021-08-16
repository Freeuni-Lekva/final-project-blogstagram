package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;

import javax.swing.plaf.nimbus.State;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlogTitleValidator implements Validator{
    private static final int TITLE_MIN = 10;
    private static final int TITLE_MAX = 100;
    private final String title;
    private final List <GeneralError> list;

    public BlogTitleValidator(String title){
        this.title = title;
        list = new ArrayList<>();
    }


    @Override
    public boolean validate() throws SQLException {
        if(title == null) {
            list.add(new VariableError("title", "Blog Title can't be null."));
            return false;
        }

        if(title.length() > TITLE_MAX || title.length() < TITLE_MIN) {
            list.add(new VariableError("title", "Blog Title must be between (" + TITLE_MIN + " " + TITLE_MAX + ") symbols." ));
            return false;
        }
        return true;

    }

    @Override
    public List<GeneralError> getErrors() {
        return list;
    }
}
