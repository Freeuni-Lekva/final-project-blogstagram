package org.blogstagram.followSystem.Servlets;

import org.blogstagram.Validators.FollowRequestValidator;
import org.blogstagram.dao.FollowDao;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.NotLoggedInException;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.followSystem.api.ResponseJson;
import org.blogstagram.followSystem.api.StatusCodes;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/followResponse")
public class followResponseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //implement logic if response is accept then write follow reqest to database.
        String toIdStr = (String) request.getSession().getAttribute("to_id");
        String fromIdStr = (String) request.getAttribute("from_id");
        Integer toId = Integer.parseInt(toIdStr);
        Integer fromId = Integer.parseInt(fromIdStr);
        UserDAO userDao = (UserDAO) request.getServletContext().getAttribute("UserDao");
        FollowRequestValidator validator = new FollowRequestValidator();
        validator.setUserDao(userDao);
        FollowDao followDao = (SqlFollowDao) request.getServletContext().getAttribute("SqlFollowDao");
        JSONObject responseJson = ResponseJson.initResponseJson();
        try {
            if(validator.isLoggedIn(toIdStr) && validator.isIdValid(fromIdStr)){
                if(request.getParameter("Accept") != null){
                    FollowApi api = new FollowApi();
                    api.setFollowDao(followDao);
                    api.setUserDao(userDao);
                    api.registerFollowRequestSender(null); // notifications
                    api.acceptFollowRequest(fromId, toId);
                    responseJson.append("status", StatusCodes.requestApproved);
                }else if(request.getParameter("Decline") != null){
                    // decline  so it must be forwarded to notification api.
                    responseJson.append("status", StatusCodes.requestDeclined);
                }else{
                    throw new IllegalAccessException("Illegal request.");
                }

            }
        } catch (NotValidUserIdException | DirectionalFollowNotAdded | DatabaseError |
                NotLoggedInException | IllegalAccessException  e) {
            responseJson.append("status", StatusCodes.error);
            responseJson.append("errorMessage", e.toString());
        } finally {
            response.getWriter().print(responseJson);
        }
    }
}
