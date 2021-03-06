package org.blogstagram.login;

import com.google.gson.Gson;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.models.User;
import org.blogstagram.validators.LoginValidator;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private boolean isUserLoggedIn(HttpServletRequest req) {
        Integer userID = (Integer) req.getSession().getAttribute("currentUserID");
        String nickname = (String) req.getSession().getAttribute("currentUserNickname");
        return userID != null && nickname != null;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(isUserLoggedIn(request)) {
            response.sendRedirect("/blogstagram/");
            return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/login/login.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection dbConnection = getConnectionFromContext(request);
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        LoginValidator validator = new LoginValidator(email, password, dbConnection);
        try {
            if(!validator.validate()) {
                List<GeneralError> errors = validator.getErrors();
                Gson gson = new Gson();
                response.getWriter().print(gson.toJson(errors));
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        UserDAO userDAO = getUserDaoFromSession(request);
        try {
            User user = userDAO.getUserByEmail(email);
            request.getSession().setAttribute("currentUserID", user.getId());
            request.getSession().setAttribute("currentUserNickname", user.getNickname());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}