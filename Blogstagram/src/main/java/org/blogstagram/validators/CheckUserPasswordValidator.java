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

public class CheckUserPasswordValidator implements Validator{

    public static final String CHECK_USER_WITH_ID_AND_PASSWORD_QUERY = "SELECT * FROM users WHERE id = ? AND password = ?";
    public static final String PASSWORD_NOT_CORRECT_ERROR = "Old password is incorrect";

    private String password;
    private Integer userID;
    private Connection connection;
    private List<GeneralError> errors;


    public CheckUserPasswordValidator(Integer userID,String password,Connection connection){
        if(connection == null)
            throw new IllegalArgumentException("Connection must not be null");
        this.userID = userID;
        this.password = password;
        this.connection = connection;
        errors = new ArrayList<>();
    }
    @Override
    public boolean validate() throws SQLException {
        errors = new ArrayList<>();

        String hashedPassword = StringHasher.hashString(password);

        PreparedStatement stm = connection.prepareStatement(CHECK_USER_WITH_ID_AND_PASSWORD_QUERY);
        stm.setInt(1,userID);
        stm.setString(2,hashedPassword);

        ResultSet resultSet = stm.executeQuery();
        if(!resultSet.next()){
            errors.add(new VariableError("old_password",PASSWORD_NOT_CORRECT_ERROR));
        }

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}