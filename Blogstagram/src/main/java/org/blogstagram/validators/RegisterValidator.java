package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.pairs.StringPair;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidator implements Validator{

    public static final String PASSWORDS_DO_NOT_MATCH_ERROR = "Passwords do not match";

    private Connection connection;
    private final String firstname;
    private final String lastname;
    private final String nickname;
    private final String email;
    private final Integer gender;
    private final Integer privacy;
    private final String password;
    private final String repeatPassword;


    private List<GeneralError> errors;

    public RegisterValidator(String firstname, String lastname, String nickname, String email, Integer gender, Integer privacy, String password, String repeatPassword,Connection connection) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.privacy = privacy;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.connection = connection;

        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() throws SQLException {
        errors = new ArrayList<>();

        /*
         *   Error Priority 1:
         *      If any of necessary variables were not included
         */

        /*
         *   Error Priority 2:
         *      If there are any general information errors, like variable lengths and some variable restrictions
         */
        EmailFormatValidator emailFormatValidator = new EmailFormatValidator(email);
        if(!emailFormatValidator.validate()){
            errors.addAll(emailFormatValidator.getErrors());
        }
        List<StringPair> generalInformationPairs = Arrays.asList(new StringPair("firstname",firstname),new StringPair("lastname",lastname),
                                                                 new StringPair("nickname",nickname));
        VariableLengthValidator variableLengthValidator = new VariableLengthValidator(generalInformationPairs);
        if(!variableLengthValidator.validate()){
            errors.addAll(variableLengthValidator.getErrors());
        }

        IllegalCharactersValidator illegalCharactersValidator = new IllegalCharactersValidator(generalInformationPairs);
        if(!illegalCharactersValidator.validate()){
            errors.addAll(illegalCharactersValidator.getErrors());
        }

        GenderValidator genderValidator = new GenderValidator(gender);
        PrivacyValidator privacyValidator = new PrivacyValidator(privacy);
        if(!genderValidator.validate())
            errors.addAll(genderValidator.getErrors());
        if(!privacyValidator.validate())
            errors.addAll(privacyValidator.getErrors());


        /* Each value must contain at least 1 character */
        Pattern CP = Pattern.compile("[A-Z]");
        for(StringPair pair: generalInformationPairs){
            String value = pair.getValue().toUpperCase();
            Matcher CM = CP.matcher(value);
            if(!CM.find()){
                String key = String.valueOf(pair.getKey().charAt(0)).toUpperCase() + pair.getKey().substring(1);
                errors.add(new VariableError(pair.getKey(),key + " must contain at least 1 character"));
            }
        }

        /*
         *  Error Priority 3:
         *      If the email or nickname is not unique
         */
        UserUniqueValidator uniquenessValidator = new UserUniqueValidator(email,nickname,connection);
        if(!uniquenessValidator.validate()){
            List<GeneralError> uniqueErrors = uniquenessValidator.getErrors();
            errors.addAll(uniqueErrors);
        }

        /*
         *  Error Priority 4:
         *      If there are any password restriction errors like length and characteristics
         */
        PasswordFormatValidator passwordFormatValidator = new PasswordFormatValidator(password);
        if(!passwordFormatValidator.validate()){
            List<GeneralError> passwordErrors = passwordFormatValidator.getErrors();
            errors.addAll(passwordErrors);
        }
        if(!password.equals(repeatPassword)){
            errors.add(new VariableError("password_confirmation",PASSWORDS_DO_NOT_MATCH_ERROR));
        }

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
