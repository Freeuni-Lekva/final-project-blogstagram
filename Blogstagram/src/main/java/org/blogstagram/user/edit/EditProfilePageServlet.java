package org.blogstagram.user.edit;

import com.google.gson.Gson;
import org.blogstagram.StringHasher;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.models.User;
import org.blogstagram.validators.EditGeneralValidator;
import org.blogstagram.validators.EditPasswordValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/edit/profile")
public class EditProfilePageServlet extends HttpServlet {

    private static final String[] editStatuses = {"EditGeneral","EditPicture","EditPassword"};

    public static final String EDIT_PROFILE_PAGE_PATH = "/jsp/user/edit/editProfilePage.jsp";


    private boolean isStatusCorrect(String status){
        for(String editStatus: editStatuses){
            if(editStatus.equals(status))
                return true;
        }
        return false;
    }
    private boolean isUserLoggedIn(HttpServletRequest req) {
        Integer userID = (Integer) req.getSession().getAttribute("currentUserID");
        String nickname = (String) req.getSession().getAttribute("currentUserNickname");
        if(userID == null || nickname == null)
            return false;
        return true;
    }
    private UserDAO getUserDaoFromContext(HttpServletRequest req){
        UserDAO userDAO = (UserDAO) req.getSession().getAttribute("UserDAO");
        return userDAO;
    }
    private Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection) req.getServletContext().getAttribute("dbConnection");
        return connection;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        /* Basic Check If User Is Logged In */
        if(!isUserLoggedIn(req)){
            res.sendRedirect("/login");
            return;
        }

        String currentUserNickname = (String)req.getSession().getAttribute("currentUserNickname");
        UserDAO userDAO = getUserDaoFromContext(req);

        User user = null;
        try {
            user = userDAO.getUserByNickname(currentUserNickname);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        req.setAttribute("CurrentUser",user);
        req.getRequestDispatcher(EDIT_PROFILE_PAGE_PATH).forward(req,res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        if(!isUserLoggedIn(req)){
            return;
        }

        String status = req.getParameter("status");

        UserDAO userDAO = getUserDaoFromContext(req);
        Connection connection = getConnectionFromContext(req);


        if(!isStatusCorrect(status)){
            Gson gson = new Gson();
            res.getWriter().print(gson.toJson("{ status: 'status incorrect' }"));
            return;
        }

        if(status.equals(editStatuses[0])){
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


        } else if(status.equals(editStatuses[1])){
            /*
             * Logic for picture edit
             */
        } else {
            /*
             * Logic for password edit
             */
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
}
