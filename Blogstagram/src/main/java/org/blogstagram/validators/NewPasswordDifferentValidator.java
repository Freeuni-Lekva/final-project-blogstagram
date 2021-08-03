package org.blogstagram.validators;

import org.blogstagram.StringHasher;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewPasswordDifferentValidator implements Validator{

    public static final String OLD_AND_NEW_PASSWORDS_ARE_SAME_ERROR = "New password must be different from current password";

    private static final String GET_USER_PASSWORD_QUERY = "SELECT password FROM users WHERE id = ?";

    private final Integer userID;
    private final String password;
    private final Connection connection;
    private List<GeneralError> errors;

    public NewPasswordDifferentValidator(Integer userID,String password,Connection connection){
        if(connection == null)
            throw new RuntimeException("Connection must not be null");

        this.userID = userID;
        this.password = password;
        this.connection = connection;
        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() throws SQLException {
        errors = new ArrayList<>();

        PreparedStatement stm = connection.prepareStatement(GET_USER_PASSWORD_QUERY);
        stm.setInt(1,userID);
        ResultSet result = stm.executeQuery();
        result.next();
        String userPassword = result.getString(1);
        if(userPassword.equals(StringHasher.hashString(password))){
            errors.add(new VariableError("new_password",OLD_AND_NEW_PASSWORDS_ARE_SAME_ERROR));
        }
        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
