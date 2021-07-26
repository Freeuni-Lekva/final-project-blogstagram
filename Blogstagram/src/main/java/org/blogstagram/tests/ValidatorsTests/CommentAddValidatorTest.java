package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.blogstagram.validators.CommentAddValidator;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Random;

public class CommentAddValidatorTest extends TestCase {
    CommentAddValidator commentAddValidator;
    Random rand;

    @Override
    protected void setUp(){
        commentAddValidator = new CommentAddValidator();
        rand = new Random();
    }

    @Test
    public void testEmpty() throws SQLException {
        String comment = "";
        assertFalse(commentAddValidator.validate(comment, ""));
    }

    @Test
    public void testOverLimit() throws SQLException {
        StringBuilder comment = new StringBuilder();
        for(int i = 0; i < 600; i++){
            comment.append('a' + i % 26);
        }
        assertFalse(commentAddValidator.validate(comment.toString(), ""));
    }

    @Test
    public void testCyclic() throws SQLException {
        for(int i = 0; i < 20; i++){
            StringBuilder comment = new StringBuilder();
            int commentLength = rand.nextInt(1000);
            for(int j = 0; j < commentLength; j++){
                comment.append('a');
            }
            if(commentLength > 600){
                assertFalse(commentAddValidator.validate(comment.toString(), ""));
            }else{
                assertTrue(commentAddValidator.validate(comment.toString(), ""));
            }

        }
    }
}
