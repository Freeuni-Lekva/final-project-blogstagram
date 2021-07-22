package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailFormatValidator implements Validator{

    public static final String EMAIL_SYNTAX_ERROR_MESSAGE = "Email syntax is incorrect";

    private String email;
    private List<GeneralError> errors;

    public EmailFormatValidator(String email){
        this.email = email;
        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() {
        errors = new ArrayList<>();

        Pattern EP = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher EM = EP.matcher(email);
        if(EM.find()){
            return true;
        }
        GeneralError error = new VariableError("email",EMAIL_SYNTAX_ERROR_MESSAGE);
        errors = new ArrayList<>();
        errors.add(error);

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
