package org.blogstagram.register;

import com.google.gson.Gson;
import org.blogstagram.StringHasher;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.GeneralError;
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

    private boolean isUserLoggedIn(HttpServletRequest req) {
        Integer userID = (Integer) req.getSession().getAttribute("currentUserID");
        String nickname = (String) req.getSession().getAttribute("currentUserNickname");
        if(userID == null || nickname == null)
            return false;
        return true;
    }

    private Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection)req.getServletContext().getAttribute("dbConnection");
        return connection;
    }
    private UserDAO getUserDaoFromSession(HttpServletRequest req){
        UserDAO userDAO = (UserDAO) req.getSession().getAttribute("UserDAO");
        return userDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if(isUserLoggedIn(req)) {
            res.sendRedirect("/");
            return;
        }
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

        Connection dbconnection = getConnectionFromContext(req);

        RegisterValidator registerValidator = new RegisterValidator(firstname,lastname,nickname,email,gender,privacy,password,repeatPassword,dbconnection);

        List<GeneralError> errors = new ArrayList<>();
        try {
            if(!registerValidator.validate())
                errors = registerValidator.getErrors();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        // If there are any errors, send to client
        if(errors.size() != 0){
            Gson gson = new Gson();
            res.getWriter().print(gson.toJson(errors));
            return;
        }

        UserDAO userDAO = getUserDaoFromSession(req);
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
