package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserUniqueValidator implements Validator{

    private static final String TABLE_NAME = "users";

    public static final String EMAIL_NOT_UNIQUE_ERROR = "Email is already taken";
    public static final String NICKNAME_NOT_UNIQUE_ERROR = "Nickname is already taken";

    private static final String EMAIL_QUERY = "SELECT * FROM " + TABLE_NAME + " where email = ?";
    private static final String NICKNAME_QUERY = "SELECT * FROM " + TABLE_NAME + " where nickname = ?";


    private Connection connection;
    private String email;
    private String nickname;
    private List<GeneralError> errors;

    public UserUniqueValidator(String email,String nickname,Connection connection) {
        if(email == null && nickname == null)
            throw new IllegalArgumentException("Email or nickname must be included");
        if(connection == null)
            throw new IllegalArgumentException("Database connection must not be null");

        this.email = email;
        this.nickname = nickname;
        this.connection = connection;
        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() throws SQLException {
        errors = new ArrayList<>();

        /* EMAIL CHECK */
        if(email != null){
            PreparedStatement stm = connection.prepareStatement(EMAIL_QUERY);
            stm.setString(1,email);
            ResultSet result = stm.executeQuery();
            if(result.next())
                errors.add(new VariableError("email",EMAIL_NOT_UNIQUE_ERROR));
        }

        /* NICKNAME CHECK */
        if(nickname != null){
            PreparedStatement stm = connection.prepareStatement(NICKNAME_QUERY);
            stm.setString(1,nickname);
            ResultSet result = stm.executeQuery();
            if(result.next())
                errors.add(new VariableError("nickname",NICKNAME_NOT_UNIQUE_ERROR));
        }

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
