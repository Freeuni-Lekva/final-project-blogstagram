package org.blogstagram.register;

import com.google.gson.Gson;
import org.blogstagram.StringHasher;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.User;
import org.blogstagram.validators.RegisterValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterServlet extends HttpServlet {

    private static final String REGISTER_PAGE_PATH = "/jsp/register/register.jsp";

    private Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection)req.getServletContext().getAttribute("dbConnection");
        return connection;
    }
    private UserDAO getUserDaoFromContext(HttpServletRequest req){
        UserDAO userDAO = (UserDAO) req.getServletContext().getAttribute("UserDAO");
        return userDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher(REGISTER_PAGE_PATH).forward(req,res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String nickname = req.getParameter("nickname");
        String email = req.getParameter("email");
        String birthday = req.getParameter("birthday");
        Integer gender = Integer.parseInt(req.getParameter("gender"));
        Integer privacy = Integer.parseInt(req.getParameter("privacy"));
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("password_confirmation");

        RegisterValidator validator = new RegisterValidator(firstname,lastname,nickname,email,gender,privacy,password,repeatPassword);
        /*
         *   Error Priority 1:
         *      If any of necessary variables were not included
         */
        List<String> notIncludedVariables = validator.getNotIncludedVariables();
        if(notIncludedVariables.size() != 0){
            Gson gson = new Gson();
            res.getWriter().print(gson.toJson(notIncludedVariables));
            return;
        }

        List<VariableError> errors = new ArrayList<>();
        /*
         *   Error Priority 2:
         *      If there are any general information errors, like variable lengths and some variable restrictions
         */
        List<VariableError> generalInformationErrors = validator.getGeneralInformationErrors();
        if(generalInformationErrors.size() != 0){
            errors.addAll(generalInformationErrors);
        }
        /*
         *  Error Priority 3:
         *      If the email or nickname is not unique
         */
        Connection dbconnection = getConnectionFromContext(req);
        try {
            if(!validator.isEmailUnique(dbconnection)){
                errors.add(new VariableError("email","Email is already taken"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        try {
            if(!validator.isNicknameUnique(dbconnection)){
                errors.add(new VariableError("nickname","Nickname is already taken"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        /*
         *  Error Priority 4:
         *      If there are any password restriction errors like length and characteristics
         */
        List<VariableError> passwordErrors = validator.getPasswordErrors();
        if(passwordErrors.size() != 0){
            errors.addAll(passwordErrors);
        }

        // If there are any errors, send to client
        if(errors.size() != 0){
            Gson gson = new Gson();
            res.getWriter().print(gson.toJson(errors));
            return;
        }

        UserDAO userDAO = getUserDaoFromContext(req);
        User user = new User(null,firstname,lastname,nickname,User.DEFAULT_ROLE,email,gender,privacy,Date.valueOf(birthday),
                                User.DEFAULT_USER_IMAGE_PATH,null,null,null,null,new Date(System.currentTimeMillis()));

        String hashedPassword = StringHasher.hashString(password);
        try {
            userDAO.addUser(user,hashedPassword);
            req.getSession().setAttribute("currentUserID",user.getId());
            req.getSession().setAttribute("currentUserNickname",user.getNickname());
            //res.sendRedirect("/user/"+user.getNickname());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }
}
