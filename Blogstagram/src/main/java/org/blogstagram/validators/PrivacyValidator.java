package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.User;

import java.util.ArrayList;
import java.util.List;

public class PrivacyValidator implements Validator{


    public static final String PRIVACY_ERROR = "Privacy must be " + User.PUBLIC + " (Public) or " + User.PRIVATE + " (Private)";

    private List<GeneralError> errors;
    private final Integer privacy;

    public PrivacyValidator(Integer privacy){
        this.privacy = privacy;
        errors = new ArrayList<>();
    }
    public PrivacyValidator(String privacy){
        this(Integer.parseInt(privacy));
    }

    @Override
    public boolean validate() {
        errors = new ArrayList<>();
        if(!privacy.equals(User.PRIVATE) && !privacy.equals(User.PUBLIC))
            errors.add(new VariableError("privacy",PRIVACY_ERROR));

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
