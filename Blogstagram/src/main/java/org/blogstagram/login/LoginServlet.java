package org.blogstagram.login;

import com.google.gson.Gson;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.VariableError;
import org.blogstagram.models.User;

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
        List<VariableError> errors = new ArrayList<>();
        LoginValidator validator = new LoginValidator(email, password);
        try {
            if(!validator.emailExists(dbConnection))
                errors.add(new VariableError("email", LoginValidator.NON_EXISTENT_EMAIL_ERROR));
            else if(!validator.passwordMatch(dbConnection)) {
                errors.add(new VariableError("password", LoginValidator.INCORRECT_PASSWORD_ERROR));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(errors.size() != 0) {
            Gson gson = new Gson();
            response.getWriter().print(gson.toJson(errors));
            return;
        }

        UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute("UserDAO");
        String query = "SELECT id, nickname FROM users WHERE email = ?;";
        try {
            PreparedStatement statement = dbConnection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int id = resultSet.getInt(1);
            String nickname = resultSet.getString(2);
            User user = userDAO.getUserByIdOrNickname(id, nickname);
            request.getSession().setAttribute("currentUserID", user.getId());
            request.getSession().setAttribute("currentUserNickname", user.getNickname());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
