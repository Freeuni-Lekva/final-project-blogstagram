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

public class UserExistsValidator {
    private static final String NON_EXISTENT_EMAIL_ERROR = "Email doesn't exist";
    private static final String INCORRECT_PASSWORD_ERROR = "Password is incorrect";
    private static final Integer NO_USER = -1;

    private final Connection connection;
    private final String email;
    private final String password;
    List<GeneralError> errors;

    public UserExistsValidator(Connection connection, String email, String password) {
        this.connection = connection;
        this.email = email;
        this.password = password;
        errors = new ArrayList<>();
    }


    public boolean exists() throws SQLException {
        EmailExistsValidator emailExistsValidator = new EmailExistsValidator(connection, email);
        if(!emailExistsValidator.emailExists()) {
            errors.add(new VariableError("email", NON_EXISTENT_EMAIL_ERROR));
            return false;
        }
        String hashCode = StringHasher.hashString(this.password);
        String query = "SELECT password, id FROM users WHERE email = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        String correctPassword = resultSet.getString(1);
        if(!hashCode.equals(correctPassword)) {
            errors.add(new VariableError("password", INCORRECT_PASSWORD_ERROR));
            return false;
        }
        return true;
    }
    

    public List<GeneralError> getErrors() {
        return errors;
    }
}