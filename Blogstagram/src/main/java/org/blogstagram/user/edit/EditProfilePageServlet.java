package org.blogstagram.user.edit;

import com.google.gson.Gson;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/edit/profile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
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
    private UserDAO getUserDaoFromSession(HttpServletRequest req){
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
        UserDAO userDAO = getUserDaoFromSession(req);

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

        if(!isStatusCorrect(status)){
            Gson gson = new Gson();
            res.getWriter().print(gson.toJson("{ status: 'status incorrect' }"));
            return;
        }

        if(status.equals(editStatuses[0])){
            EditProfile.editGeneral(req,res);

        } else if(status.equals(editStatuses[1])){
            /*
             * Logic for picture edit
             */
            String nickname = (String)req.getSession().getAttribute("currentUserNickname");
            String uploadPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator + nickname;

            try {
                EditProfile.editPicture(req,res,uploadPath);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } else {
            EditProfile.editPassword(req,res);
        }
    }
}
