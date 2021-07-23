package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.pairs.GeneralPair;
import org.blogstagram.pairs.StringKeyPair;
import org.blogstagram.pairs.StringPair;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditGeneralValidator implements Validator{

    public static final String BIO_ERROR = "Bio shouldn't be longer than 255 symbols";

    private Connection connection;
    private final Integer userID;
    private final String firstname;
    private final String lastname;
    private final String nickname;
    private final String email;
    private final Integer gender;
    private final Integer privacy;
    private final String country;
    private final String city;
    private final String website;
    private final String bio;

    private List<GeneralError> errors;

    public EditGeneralValidator(Integer userID,String firstname, String lastname, String nickname, String email, Integer gender, Integer privacy, String country, String city,String website,String bio,Connection connection){
        if(connection == null)
            throw new IllegalArgumentException("Connection must not be null");

        this.userID = userID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.privacy = privacy;
        this.country = country;
        this.city = city;
        this.website = website;
        this.bio = bio;
        this.connection = connection;
        errors = new ArrayList<>();
    }

    @Override
    public boolean validate() throws SQLException {
        errors = new ArrayList<>();

        List<GeneralPair> pairs = new ArrayList<>();
        pairs.add(new StringPair("firstname",firstname));
        pairs.add(new StringPair("lastname",lastname));
        pairs.add(new StringPair("nickname",nickname));
        pairs.add(new StringPair("email",email));
        pairs.add(new StringKeyPair<Integer>("gender",gender));
        pairs.add(new StringKeyPair<Integer>("privacy",privacy));
        if(country != null)
            pairs.add(new StringPair("country",country));
        if(city != null)
            pairs.add(new StringPair("city",city));
        if(website != null)
            pairs.add(new StringPair("website",website));
        if(bio != null)
            pairs.add(new StringPair("bio",bio));

        for(GeneralPair pair: pairs){
            String key = (String)pair.getKey();
            Object value = pair.getValue();
            if(value == null){
                String keyText = String.valueOf(key.charAt(0)).toUpperCase() + key.substring(1);
                errors.add(new VariableError(key,keyText + " must be included"));
            }
        }
        if(errors.size() != 0){
            return false;
        }
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
        UserUniqueValidator uniquenessValidator = new UserUniqueValidator(userID,email,nickname,connection);
        if(!uniquenessValidator.validate()){
            List<GeneralError> uniqueErrors = uniquenessValidator.getErrors();
            errors.addAll(uniqueErrors);
        }

        if(bio != null && bio.length() > 255){
            errors.add(new VariableError("bio",BIO_ERROR));
        }

        return errors.size() == 0;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
