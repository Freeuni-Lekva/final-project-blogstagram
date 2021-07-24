package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.blogstagram.validators.GenderValidator;

public class GenderValidatorTest extends TestCase {
    public void test1(){
        GenderValidator validator = new GenderValidator(0);
        assertTrue(validator.validate());
        assertEquals(0,validator.getErrors().size());
    }
    public void test2(){
        GenderValidator validator = new GenderValidator(1);
        assertTrue(validator.validate());
        assertEquals(0,validator.getErrors().size());
    }
    public void test3(){
        for(int i=2; i<10; i++){
            GenderValidator validator = new GenderValidator(i);
            assertFalse(validator.validate());
            assertTrue(validator.getErrors().size() > 0);
        }

    }
    public void test4(){
        for(int i=-5; i<0; i++){
            GenderValidator validator = new GenderValidator(i);
            assertFalse(validator.validate());
            assertTrue(validator.getErrors().size() > 0);
        }
    }
}
