package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.pairs.StringPair;
import org.blogstagram.validators.VariableLengthValidator;

import java.util.Arrays;
import java.util.List;

public class VariableLengthValidatorTest extends TestCase {

    private static final String VARIABLE_FIELD = "elvariablos";

    public void test1(){

        VariableLengthValidator validator = new VariableLengthValidator(new StringPair(VARIABLE_FIELD,"u"));
        assertFalse(validator.validate());

        VariableError error = (VariableError) validator.getErrors().get(0);
        assertEquals(VARIABLE_FIELD,error.getVariableName());
    }
    public void test2(){
        String variable = "123456789101112131415161718";
        VariableLengthValidator validator = new VariableLengthValidator(new StringPair(VARIABLE_FIELD,variable));
        assertFalse(validator.validate());

        assertTrue(validator.getErrors().size() > 0);
    }
    public void test3(){
        String variable = "Elos Gvaramos";
        VariableLengthValidator validator = new VariableLengthValidator(new StringPair(VARIABLE_FIELD,variable));
        assertTrue(validator.validate());
        assertEquals(0,validator.getErrors().size());
    }
    public void test4(){
        List<StringPair> pairs = Arrays.asList(new StringPair("variable1","u")
                                                ,new StringPair("variable2","uu")
                                                ,new StringPair("variable3","normal"));

        VariableLengthValidator validator = new VariableLengthValidator(pairs);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();

        for(int i=0; i<errors.size(); i++){
            String variable = ((VariableError)errors.get(i)).getVariableName();
            assertEquals("variable"+(i+1),variable);
        }


    }
}
