package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.pairs.StringPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IllegalCharactersValidator implements Validator{

    private static final String ILLEGAL_CHARACTERS = "\\/:*?\"<> |~#%&+{}-";
    private static final String ILLEGAL_CHARACTERS_ERROR = "must not contain illegal characters";

    private List<StringPair> pairs;
    private List<GeneralError> errors;

    public IllegalCharactersValidator(List<StringPair> pairs){
        this.pairs = pairs;
        errors = new ArrayList<>();
    }
    public IllegalCharactersValidator(StringPair pair){
        this(Collections.singletonList(pair));
    }

    private boolean containsIllegalCharacters(String str){
        for(int i=0; i < ILLEGAL_CHARACTERS.length(); i++){
            String currentIllegalCharacter = Character.toString(ILLEGAL_CHARACTERS.charAt(i));
            if(str.contains(currentIllegalCharacter))
                return true;
        }
        return false;
    }

    @Override
    public boolean validate() {
        errors = new ArrayList<>();
        for(StringPair pair: pairs){
            if(containsIllegalCharacters(pair.getValue())){
                String key = String.valueOf(pair.getKey().charAt(0)).toUpperCase() + pair.getKey().substring(1);
                errors.add(new VariableError(pair.getKey(),key + " " + ILLEGAL_CHARACTERS_ERROR));
            }

        }
        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
