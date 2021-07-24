package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.blogstagram.validators.PrivacyValidator;

public class PrivacyValidatorTest extends TestCase {

    public void test1(){
        PrivacyValidator validator = new PrivacyValidator(0);
        assertTrue(validator.validate());
        assertEquals(0,validator.getErrors().size());
    }
    public void test2(){
        PrivacyValidator validator = new PrivacyValidator(1);
        assertTrue(validator.validate());
        assertEquals(0,validator.getErrors().size());
    }
    public void test3(){
        for(int i=2; i<10;i++){
            PrivacyValidator validator = new PrivacyValidator(i);
            assertFalse(validator.validate());
            assertTrue(validator.getErrors().size() > 0);
        }
    }
    public void test4(){
        for(int i=-5; i<0; i++){
            PrivacyValidator validator = new PrivacyValidator(i);
            assertFalse(validator.validate());
            assertTrue(validator.getErrors().size() > 0);
        }
    }
}
