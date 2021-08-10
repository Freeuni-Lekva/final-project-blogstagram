package org.blogstagram.admin;

import com.google.gson.Gson;
import org.blogstagram.dao.AdminDAO;
import org.blogstagram.errors.VariableError;
import org.blogstagram.validators.AdminValidator;
import org.blogstagram.validators.UserModeratorValidator;
import org.blogstagram.validators.UserNotModeratorValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/changeRole/user")
public class AdminModeratorServlet extends HttpServlet {
    AdminDAO adminDAO;
    private static final String MAKE_MODERATOR = "MakeModer";
    private static final String MAKE_USER = "MakeUser";

    private boolean isCorrect(String request){
        if(request.equals(MAKE_MODERATOR) || request.equals(MAKE_USER)){
            return true;
        }
        return true;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("First line");
        Integer user_id = (Integer) request.getSession().getAttribute("currentUserID");
        if(user_id == null){
            response.sendError(response.SC_UNAUTHORIZED);
            return;
        }
        Connection connection = (Connection) request.getServletContext().getAttribute("dbConnection");
        adminDAO = (AdminDAO) request.getSession().getAttribute("AdminDAO");;
        String requestType = request.getParameter("OperationType");
        System.out.println(requestType + "line 45");
        if(!isCorrect(requestType)){
            Gson gson = new Gson();
            response.getWriter().print(gson.toJson("{ request: 'request incorrect' }"));
            return;
        }
        AdminValidator adminValidator = new AdminValidator();
        adminValidator.setAdminDAOUser(adminDAO, user_id, true);
        // validate that current user id is admin or moderator
        try {
            if(!adminValidator.validate()){
                Gson gson = new Gson();
                response.getWriter().print(gson.toJson("{User is not eligible to make these changes}"));
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        List<VariableError> errorList = new ArrayList<>();
        try{
            System.out.println("line 66");
            System.out.println(requestType);
            String current_user_id_str = request.getParameter("user_id");
            int current_user_id = Integer.parseInt(current_user_id_str);
            if(requestType.equals(MAKE_MODERATOR)){
                System.out.println("line 74");
                UserNotModeratorValidator notModeratorValidator = new UserNotModeratorValidator();
                notModeratorValidator.setConnectionUser(connection, current_user_id);
                if(notModeratorValidator.validate()) {
                    adminDAO.makeUserModer(current_user_id);
                }else{
                    VariableError error = new VariableError("Admin System", "User is already moderator");
                    errorList.add(error);
                }
            }else if(requestType.equals(MAKE_USER)){
                System.out.println("line 84");
                UserModeratorValidator moderatorValidator = new UserModeratorValidator();
                moderatorValidator.setConnectionUser(connection, current_user_id);
                if(moderatorValidator.validate()){
                    adminDAO.makeModerUser(current_user_id);
                }else{
                    VariableError error = new VariableError("Admin System", "Role of user is already user");
                    errorList.add(error);
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(errorList.size() != 0) {
            Gson gson = new Gson();
            response.getWriter().print(gson.toJson(errorList));
        }

    }
}
