package org.blogstagram.user.edit;

import com.google.gson.Gson;
import org.blogstagram.StringHasher;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.models.User;
import org.blogstagram.validators.EditGeneralValidator;
import org.blogstagram.validators.EditPasswordValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditProfile {

    private static final String DEFAULT_PROFILE_PICTURE_NAME = "profile.jpg";

    private static UserDAO getUserDaoFromSession(HttpServletRequest req){
        UserDAO userDAO = (UserDAO) req.getSession().getAttribute("UserDAO");
        return userDAO;
    }
    private static Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection) req.getServletContext().getAttribute("dbConnection");
        return connection;
    }


    public static void editGeneral(HttpServletRequest req,HttpServletResponse res) throws IOException {

        UserDAO userDAO = getUserDaoFromSession(req);
        Connection connection = getConnectionFromContext(req);

        Integer userID = (Integer)req.getSession().getAttribute("currentUserID");

        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String nickname = req.getParameter("nickname");
        String email = req.getParameter("email");
        String birthday = req.getParameter("birthday");
        Integer gender = Integer.parseInt(req.getParameter("gender"));
        Integer privacy = Integer.parseInt(req.getParameter("privacy"));
        String country = req.getParameter("country");
        String city = req.getParameter("city");
        String website = req.getParameter("website");
        String bio = req.getParameter("bio");

        List<GeneralError> errors = new ArrayList<>();
        EditGeneralValidator editGeneralValidator = new EditGeneralValidator(userID,firstname,lastname,nickname,email,gender,privacy,country,city,website,bio
                ,connection);

        try {
            if(!editGeneralValidator.validate()){
                errors = editGeneralValidator.getErrors();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        User newUser = null;
        if(errors.size() != 0){
            Gson gson = new Gson();
            res.getWriter().print(gson.toJson(errors));
            return;
        }
        try {
            newUser = userDAO.getUserByID(userID);
            newUser.setFirstname(firstname);
            newUser.setLastname(lastname);
            newUser.setNickname(nickname);
            newUser.setEmail(email);
            newUser.setBirthday(Date.valueOf(birthday));
            newUser.setGender(gender);
            newUser.setPrivacy(privacy);
            newUser.setCountry(country);
            newUser.setCity(city);
            newUser.setWebsite(website);
            newUser.setBio(bio);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        try {
            userDAO.updateUserGeneralInfoByID(userID,newUser);
            req.getSession().setAttribute("currentUserNickname",nickname);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }
    public static void editPicture(HttpServletRequest req,HttpServletResponse res, String uploadPath) throws IOException, ServletException, SQLException {
        UserDAO userDAO = getUserDaoFromSession(req);

        String nickname = (String)req.getSession().getAttribute("currentUserNickname");

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        Part part = req.getPart("file");
        String fileName = DEFAULT_PROFILE_PICTURE_NAME;
        part.write(uploadPath + File.separator + fileName);

        String path = "/images/"+nickname+"/"+fileName;

        userDAO.updateUserImageByNickname(nickname,path);
        res.sendRedirect("/edit/profile");
    }
    public static void editPassword(HttpServletRequest req, HttpServletResponse res) throws IOException {
        UserDAO userDAO = getUserDaoFromSession(req);
        Connection connection = getConnectionFromContext(req);

        Integer userID = (Integer)req.getSession().getAttribute("currentUserID");
        String old_password = req.getParameter("old_password");
        String new_password = req.getParameter("new_password");
        String new_password_confirmation = req.getParameter("new_password_confirmation");


        List<GeneralError> errors = new ArrayList<>();
        EditPasswordValidator editPasswordValidator = new EditPasswordValidator(userID,old_password,new_password,new_password_confirmation,connection);

        try {
            if(!editPasswordValidator.validate()){
                errors = editPasswordValidator.getErrors();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        if(errors.size() != 0){
            Gson gson = new Gson();
            res.getWriter().print(gson.toJson(errors));
            return;
        }

            /*
                    Change data in database
             */
        String hashedPassword = StringHasher.hashString(new_password);
        try {
            userDAO.updateUserPasswordByID(userID,hashedPassword);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
