package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordFormatValidator implements Validator {

    public static final Integer PASSWORD_LOWER_BOUND_LENGTH = 8;
    public static final String PASSWORD_LENGTH_ERROR = "Password must contain at least 8 characters";

    public static final String PASSWORD_UPPERCASE_ERROR = "Password must contain at least 1 upper case character";
    public static final String PASSWORD_LOWERCASE_ERROR = "Password must contain at least 1 lower case character";
    public static final String PASSWORD_DIGIT_ERROR = "Password must containt at least 1 digit";


    private String password;
    private List<GeneralError> errors;

    public PasswordFormatValidator(String password){
        this.password = password;
        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() {
        errors = new ArrayList<>();

        /* Length */
        if(password.length() < PASSWORD_LOWER_BOUND_LENGTH)
            errors.add(new VariableError("password",PASSWORD_LENGTH_ERROR));

        /* Upper Case */
        Pattern UCP = Pattern.compile("[A-Z]");
        Matcher UCM = UCP.matcher(password);

        if(!UCM.find())
            errors.add(new VariableError("password",PASSWORD_UPPERCASE_ERROR));

        /* Lower Case */
        Pattern LCP = Pattern.compile("[a-z]");
        Matcher LCM = LCP.matcher(password);

        if(!LCM.find())
            errors.add(new VariableError("password",PASSWORD_LOWERCASE_ERROR));

        /* Digit */
        Pattern DP = Pattern.compile("[0-9]");
        Matcher DM = DP.matcher(password);
        if(!DM.find())
            errors.add(new VariableError("password",PASSWORD_DIGIT_ERROR));

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
