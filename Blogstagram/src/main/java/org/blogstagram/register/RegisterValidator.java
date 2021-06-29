package org.blogstagram.register;

import org.blogstagram.errors.VariableError;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidator {


    private static final String LENGTH_ERROR = "Length must be between 2 and 15 characters";
    private static final String ILLEGAL_CHARACTERS_ERROR = "Should not contain illegal characters";
    private static final int LOWER_BOUND_LENGTH = 2;
    private static final int UPPER_BOUND_LENGTH = 15;

    private static final String ILLEGAL_CHARACTERS = "\\/:*?\"<> |~#%&+{}-";


    private final String firstname;
    private final String lastname;
    private final String nickname;
    private final String email;
    /*
     *   0 - Male
     *   1 - Female
     */
    private final String gender;
    /*
     *  public / private
     */
    private final String privacy;
    private final String password;
    private final String repeatPassword;


    public RegisterValidator(String firstname, String lastname, String nickname, String email, String gender, String privacy, String password, String repeatPassword) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.privacy = privacy;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    private boolean satisfiesLengthRestrictions(String variable){
        int length = variable.length();
        if(length < LOWER_BOUND_LENGTH || length > UPPER_BOUND_LENGTH )
            return false;
        return true;
    }

    public List<String> getNotIncludedVariables(){
        List<String> notIncludedVariables = new ArrayList<>();
        if(firstname == null)
            notIncludedVariables.add("firstname");
        if(lastname == null)
            notIncludedVariables.add("lastname");
        if(nickname == null)
            notIncludedVariables.add("nickname");
        if(email == null)
            notIncludedVariables.add("email");
        if(gender == null)
            notIncludedVariables.add("gender");
        if(privacy == null)
            notIncludedVariables.add("privacy");
        if(password == null)
            notIncludedVariables.add("password");
        if(repeatPassword == null)
            notIncludedVariables.add("repeatPassword");

        return notIncludedVariables;
    }
    public boolean checkStringForIllegalCharacters(String str){
        for(int i=0; i<ILLEGAL_CHARACTERS.length();i++){
            String currentCharacter = Character.toString(ILLEGAL_CHARACTERS.charAt(i));
            if(str.contains(currentCharacter))
                return false;
        }
        return true;
    }

    private List<VariableError> getGeneralInformationLengthErrors(){
        List<VariableError> errors = new ArrayList<>();
        if(!satisfiesLengthRestrictions(firstname))
            errors.add(new VariableError("firstname",LENGTH_ERROR));

        if(!satisfiesLengthRestrictions(lastname))
            errors.add(new VariableError("lastname",LENGTH_ERROR));

        if(!satisfiesLengthRestrictions(nickname))
            errors.add(new VariableError("nickname",LENGTH_ERROR));

        return errors;
    }

    public List<VariableError> getGeneralInformationErrors(){
        List<VariableError> errors = getGeneralInformationLengthErrors();

        if(!checkStringForIllegalCharacters(firstname))
            errors.add(new VariableError("firstname",ILLEGAL_CHARACTERS_ERROR));
        if(!checkStringForIllegalCharacters(lastname))
            errors.add(new VariableError("lastname",ILLEGAL_CHARACTERS_ERROR));
        if(!checkStringForIllegalCharacters(nickname))
            errors.add(new VariableError("nickname",ILLEGAL_CHARACTERS_ERROR));

        if(!gender.equals("0") && !gender.equals("1"))
            errors.add(new VariableError("gender","Gender parameter must be 0 (Male) or 1 (Female)"));
        if(!privacy.equals("0") && !privacy.equals("0"))
            errors.add(new VariableError("privacy","Privacy parameter must be 0 (public) or 0 (private)"));

        /* EMAIL REGEX */
        Pattern EP = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher EM = EP.matcher(email);
        if(!EM.find())
            errors.add(new VariableError("email","Email syntax is incorrect"));


        if(nickname.contains(" "))
            errors.add(new VariableError("nickname","Nickname must not contain empty space"));

        return errors;
    }

    public List<VariableError> getPasswordErrors(){
        List<VariableError> errors = new ArrayList<>();

        // Password length check
        if(password.length() < 8)
            errors.add(new VariableError("password","Password length must be at least 8 characters"));

        Pattern UCP = Pattern.compile("[A-Z]");
        Pattern LCP = Pattern.compile("[a-z]");
        Pattern DP = Pattern.compile("[0-9]");

        Matcher UCM = UCP.matcher(password);
        Matcher LCM = LCP.matcher(password);
        Matcher DM = DP.matcher(password);

        // Passsowrd Upper/Lower Case And Digit Character Check
        if(!UCM.find())
            errors.add(new VariableError("password","Password must contain at least one upper case character"));
        if(!LCM.find())
            errors.add(new VariableError("password","Password must contain at least one lower case character"));
        if(!DM.find())
            errors.add(new VariableError("password","Password must contain at least one digit"));

        // Password and Repeat Password equality check
        if(!password.equals(repeatPassword))
            errors.add(new VariableError("password_confirmation","Passwords do not match"));

        return errors;
    }

    public boolean isEmailUnique(Connection connection) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
        stm.setString(1,email);
        ResultSet result = stm.executeQuery();
        int length = 0;
        while(result.next())
            length++;
        return length == 0;
    }
    public boolean isNicknameUnique(Connection connection) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM users WHERE nickname = ?");
        stm.setString(1,nickname);
        ResultSet result = stm.executeQuery();
        int length = 0;
        while(result.next())
            length++;
        return length == 0;
    }


}
