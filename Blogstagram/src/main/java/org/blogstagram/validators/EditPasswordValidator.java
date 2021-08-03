package org.blogstagram.validators;

import org.blogstagram.StringHasher;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditPasswordValidator implements Validator{

    public static final String OLD_AND_NEW_PASSWORDS_ARE_SAME_ERROR = "New password must be different from current password";

    private final Integer userID;
    private final String old_password;
    private final String new_password;
    private final String new_password_confirmation;
    private final Connection connection;

    private List<GeneralError> errors;

    public EditPasswordValidator(Integer userID,String old_password, String new_password, String new_password_confirmation, Connection connection){
        if(connection == null)
            throw new IllegalArgumentException("Connection must not be null");
        this.userID = userID;
        this.old_password = old_password;
        this.new_password = new_password;
        this.new_password_confirmation = new_password_confirmation;
        this.connection = connection;
        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() throws SQLException {
        errors = new ArrayList<>();

        if(old_password == null)
            errors.add(new VariableError("old_password","Old password must be included"));
        if(new_password == null)
            errors.add(new VariableError("new_password","New password must be included"));
        if(new_password_confirmation == null)
            errors.add(new VariableError("new_password_confirmation","New password confirmation must be included"));

        if(errors.size() != 0)
            return false;

        /*
            1) Old password is correct for UserID (Should be written validator)
            2) Old password is not same as new password (Simple)
            3) Validate new password (Have a validator)
            4) new password is same as new password confirmation(Simple)
         */

        /* Old password is correct for UserID */
        CheckUserPasswordValidator checkUserPasswordValidator = new CheckUserPasswordValidator(userID,old_password,connection);
        if(!checkUserPasswordValidator.validate()){
            errors.addAll(checkUserPasswordValidator.getErrors());
        }
        /* Old password is not same as the new password */
        NewPasswordDifferentValidator newPasswordDifferentValidator = new NewPasswordDifferentValidator(userID,new_password,connection);
        if(!newPasswordDifferentValidator.validate())
            errors.addAll(newPasswordDifferentValidator.getErrors());

        /* Validate new password format */
        PasswordFormatValidator passwordFormatValidator = new PasswordFormatValidator(new_password);
        if(!passwordFormatValidator.validate()){
            List<GeneralError> passwordFormatErrors = passwordFormatValidator.getErrors();
            for(GeneralError error: passwordFormatErrors)
                ((VariableError)error).setVariableName("new_password");
            errors.addAll(passwordFormatValidator.getErrors());
        }
        /* New password is same as new password confirmation */
        if(!new_password.equals(new_password_confirmation)){
            errors.add(new VariableError("new_password_confirmation",RegisterValidator.PASSWORDS_DO_NOT_MATCH_ERROR));
        }

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
