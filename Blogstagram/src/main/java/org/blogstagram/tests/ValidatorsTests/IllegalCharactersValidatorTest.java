package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.blogstagram.pairs.StringPair;
import org.blogstagram.validators.IllegalCharactersValidator;

public class IllegalCharactersValidatorTest extends TestCase {

    public static final String VARIABLE = "variable";

    public void test(){
        for(int i=0; i< IllegalCharactersValidator.ILLEGAL_CHARACTERS.length();i++){
            char illegal = IllegalCharactersValidator.ILLEGAL_CHARACTERS.charAt(i);
            String value = "value"+illegal;

            IllegalCharactersValidator validator = new IllegalCharactersValidator(new StringPair(VARIABLE,value));
            assertFalse(validator.validate());
            assertTrue(validator.getErrors().size() > 0);
        }
        IllegalCharactersValidator validator = new IllegalCharactersValidator(new StringPair(VARIABLE,"Helou"));
        assertTrue(validator.validate());
        assertEquals(0,validator.getErrors().size());
    }
}
