package org.blogstagram.tests.ValidatorsTests;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.StringHasher;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.User;
import org.blogstagram.validators.EditPasswordValidator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

public class EditPasswordValidatorTest extends TestCase {
    public static final String EMPTY_QUERY = "DELETE FROM users";
    private Connection connection;
    private Random random = new Random();
    private UserDAO userDAO;

    @Override
    protected void setUp() throws Exception {
        BasicDataSource source = new BasicDataSource();
        source.setUsername("root");
        source.setPassword(""); // local password
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb_test");
        try {
            this.connection = source.getConnection();
            userDAO = new UserDAO(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
    public User createDummyUser(int identificator){

        String firstname = "firstname"+String.valueOf(identificator);
        String lastname = "lastname"+String.valueOf(identificator);
        String nickname = "nickname"+String.valueOf(identificator);
        String email = "email"+String.valueOf(identificator)+"@gmail.com";
        Date birthday = Date.valueOf("2001-08-07");
        Integer gender = random.nextInt(2);
        Integer privacy = random.nextInt(2);
        String image = "image"+String.valueOf(identificator);
        String country = "Georgia";
        String city = "Tbilisi";
        String website = "website"+String.valueOf(identificator);
        String bio ="Xelou Gaiz "+String.valueOf(identificator);
        Date createdAt = new Date(System.currentTimeMillis());

        User user = new User(null,firstname,lastname,nickname,User.DEFAULT_ROLE,email,gender,privacy,birthday,image,country,city,website,bio,createdAt);
        return user;
    }

    public void test1() throws SQLException {
        User user = createDummyUser(1);
        userDAO.addUser(user, StringHasher.hashString("Password1"));

        EditPasswordValidator validator = new EditPasswordValidator(user.getId(),"Password1","Password2","Password2",connection);
        assertTrue(validator.validate());
        assertEquals(0,validator.getErrors().size());
    }
    public void test2() throws SQLException {
        User user = createDummyUser(2);
        userDAO.addUser(user,StringHasher.hashString("Password1"));

        EditPasswordValidator validator = new EditPasswordValidator(user.getId(),"Password1","Password2","Password3",connection);
        assertFalse(validator.validate());

        List<GeneralError> errors = validator.getErrors();
        assertEquals(1,errors.size());

        VariableError error = ((VariableError)errors.get(0));
        assertEquals("new_password_confirmation",error.getVariableName());
    }
    public void test3() throws SQLException{
        User user = createDummyUser(3);
        userDAO.addUser(user,StringHasher.hashString("Password1"));

        EditPasswordValidator validator = new EditPasswordValidator(user.getId(),"Password2","Password3","Password3",connection);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(1,errors.size());

        VariableError error = ((VariableError)errors.get(0));
        assertEquals("old_password",error.getVariableName());
    }
    public void test4() throws SQLException {
        User user = createDummyUser(4);
        userDAO.addUser(user,StringHasher.hashString("Password1"));
        EditPasswordValidator validator = new EditPasswordValidator(user.getId(),"Password1","Password1","Password1",connection);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(1,errors.size());

        VariableError error = ((VariableError)errors.get(0));
        assertEquals("new_password",error.getVariableName());
        assertEquals(EditPasswordValidator.OLD_AND_NEW_PASSWORDS_ARE_SAME_ERROR,error.getErrorMessage());
    }
    public void test5() throws SQLException {
        User user = createDummyUser(5);
        userDAO.addUser(user,StringHasher.hashString("Password1"));
        EditPasswordValidator validator = new EditPasswordValidator(user.getId(),"Password1","-","Password1",connection);
        assertFalse(validator.validate());
        List<GeneralError> errors = validator.getErrors();
        assertEquals(5,errors.size());
        for(int i=0; i<errors.size();i++){
            VariableError err = (VariableError) (errors.get(i));

            if(i < errors.size()-1)
                assertEquals("new_password",err.getVariableName());
            else
                assertEquals("new_password_confirmation",err.getVariableName());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        Statement stm = connection.createStatement();
        stm.execute(EMPTY_QUERY);
    }
}
