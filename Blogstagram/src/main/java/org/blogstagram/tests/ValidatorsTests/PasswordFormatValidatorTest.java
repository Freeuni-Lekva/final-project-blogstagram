package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.blogstagram.StringHasher;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.validators.PasswordFormatValidator;

import java.util.List;

public class PasswordFormatValidatorTest extends TestCase {
    public void test1(){
        String password = "test";
        PasswordFormatValidator validator = new PasswordFormatValidator(password);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(3,errors.size());

        String error1 = errors.get(0).getErrorMessage();
        String error2 = errors.get(1).getErrorMessage();
        String error3 = errors.get(2).getErrorMessage();

        assertEquals(PasswordFormatValidator.PASSWORD_LENGTH_ERROR,error1);
        assertEquals(PasswordFormatValidator.PASSWORD_UPPERCASE_ERROR,error2);
        assertEquals(PasswordFormatValidator.PASSWORD_DIGIT_ERROR,error3);
    }
    public void test2(){
        String password = "testtest";
        PasswordFormatValidator validator = new PasswordFormatValidator(password);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(2,errors.size());

        String error1 = errors.get(0).getErrorMessage();
        String error2 = errors.get(1).getErrorMessage();

        assertEquals(PasswordFormatValidator.PASSWORD_UPPERCASE_ERROR,error1);
        assertEquals(PasswordFormatValidator.PASSWORD_DIGIT_ERROR,error2);
    }
    public void test3(){
        String password = "testest1";
        PasswordFormatValidator validator = new PasswordFormatValidator(password);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(1,errors.size());

        String error1 = errors.get(0).getErrorMessage();
        assertEquals(PasswordFormatValidator.PASSWORD_UPPERCASE_ERROR,error1);
    }
    public void test4(){
        String password = "Testest1";
        PasswordFormatValidator validator = new PasswordFormatValidator(password);
        assertTrue(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(0,errors.size());
    }
    public void test5(){
        String password = "TEST";
        PasswordFormatValidator validator = new PasswordFormatValidator(password);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(3,errors.size());

        String error1 = errors.get(0).getErrorMessage();
        String error2 = errors.get(1).getErrorMessage();
        String error3 = errors.get(2).getErrorMessage();

        assertEquals(PasswordFormatValidator.PASSWORD_LENGTH_ERROR,error1);
        assertEquals(PasswordFormatValidator.PASSWORD_LOWERCASE_ERROR,error2);
        assertEquals(PasswordFormatValidator.PASSWORD_DIGIT_ERROR,error3);
    }
    public void test6(){
        String password = "1234";
        PasswordFormatValidator validator = new PasswordFormatValidator(password);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(3,errors.size());

        String error1 = errors.get(0).getErrorMessage();
        String error2 = errors.get(1).getErrorMessage();
        String error3 = errors.get(2).getErrorMessage();

        assertEquals(PasswordFormatValidator.PASSWORD_LENGTH_ERROR,error1);
        assertEquals(PasswordFormatValidator.PASSWORD_UPPERCASE_ERROR,error2);
        assertEquals(PasswordFormatValidator.PASSWORD_LOWERCASE_ERROR,error3);
    }
    public void test7(){
        String password ="-";
        PasswordFormatValidator validator = new PasswordFormatValidator(password);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(4,errors.size());

        String error1 = errors.get(0).getErrorMessage();
        String error2 = errors.get(1).getErrorMessage();
        String error3 = errors.get(2).getErrorMessage();
        String error4 = errors.get(3).getErrorMessage();

        assertEquals(PasswordFormatValidator.PASSWORD_LENGTH_ERROR,error1);
        assertEquals(PasswordFormatValidator.PASSWORD_UPPERCASE_ERROR,error2);
        assertEquals(PasswordFormatValidator.PASSWORD_LOWERCASE_ERROR,error3);
        assertEquals(PasswordFormatValidator.PASSWORD_DIGIT_ERROR,error4);
    }

}
