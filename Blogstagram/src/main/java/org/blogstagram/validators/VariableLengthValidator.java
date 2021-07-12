package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.pairs.StringPair;

import java.util.*;

public class VariableLengthValidator implements Validator{

    public static final Integer LOWER_BOUND_LENGTH = 2;
    public static final Integer UPPER_BOUND_LENGTH = 15;
    public static final String LENGTH_ERROR = "length must be between " + LOWER_BOUND_LENGTH + " and " + UPPER_BOUND_LENGTH;

    private List<StringPair> pairs;
    private List<GeneralError> errors;

    public VariableLengthValidator(List<StringPair> pairs){
        this.pairs = pairs;
        errors = new ArrayList<>();
    }
    public VariableLengthValidator(StringPair pair){
        this(Collections.singletonList(pair));
    }

    @Override
    public boolean validate() {
        errors = new ArrayList<>();
        for(StringPair pair: pairs){
            String str = pair.getValue();
            if(str.length() < LOWER_BOUND_LENGTH || str.length() > UPPER_BOUND_LENGTH){
                String key = String.valueOf(pair.getKey().charAt(0)).toUpperCase() + " " + pair.getKey().substring(1);
                errors.add(new VariableError(pair.getKey(),key + " " + LENGTH_ERROR));
            }
        }

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
