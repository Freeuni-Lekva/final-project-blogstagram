package org.blogstagram.validators;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.User;

import java.util.ArrayList;
import java.util.List;

public class GenderValidator implements Validator{


    public static final String GENDER_ERROR = "Gender must be " + User.MALE + " (Male) or " + User.FEMALE + " (Female)";

    private List<GeneralError> errors;
    private Integer gender;


    public GenderValidator(Integer gender){
        this.gender = gender;
        errors = new ArrayList<>();
    }

    public GenderValidator(String gender){
        this(Integer.parseInt(gender));
    }

    @Override
    public boolean validate() {
        errors = new ArrayList<>();

        if(!gender.equals(User.MALE) && !gender.equals(User.FEMALE))
            errors.add(new VariableError("gender",GENDER_ERROR));

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
