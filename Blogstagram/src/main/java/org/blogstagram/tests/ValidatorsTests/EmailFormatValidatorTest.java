package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.validators.EmailFormatValidator;

import java.util.List;

public class EmailFormatValidatorTest extends TestCase {

    public void test1(){
        String email = "emailos";
        EmailFormatValidator validator = new EmailFormatValidator(email);
        assertFalse(validator.validate());

        VariableError error = (VariableError) validator.getErrors().get(0);

        assertEquals("email",error.getVariableName());
        assertEquals(EmailFormatValidator.EMAIL_SYNTAX_ERROR_MESSAGE,error.getErrorMessage());
    }

    public void test2(){
        String email = "email@email";
        EmailFormatValidator validator = new EmailFormatValidator(email);
        assertFalse(validator.validate());
        assertTrue(validator.getErrors().size() > 0);
    }
    public void test3(){
        String email = "email@email.com";
        EmailFormatValidator validator = new EmailFormatValidator(email);
        assertTrue(validator.validate());

        assertEquals(0,validator.getErrors().size());
    }
}
