package org.blogstagram.login;

import com.google.gson.Gson;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.User;
import org.blogstagram.validators.LoginValidator;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/login/login.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection dbConnection = (Connection) request.getServletContext().getAttribute("dbConnection");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        LoginValidator validator = new LoginValidator(email, password, dbConnection);
        try {
            if(!validator.validation()) {
                List<GeneralError> errors = validator.getErrors();
                Gson gson = new Gson();
                response.getWriter().print(gson.toJson(errors));
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        UserDAO userDAO = (UserDAO) request.getSession().getAttribute("UserDAO");
        try {
            User user = userDAO.getUserByID(validator.getId());
            request.getSession().setAttribute("currentUserID", user.getId());
            request.getSession().setAttribute("currentUserNickname", user.getNickname());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
