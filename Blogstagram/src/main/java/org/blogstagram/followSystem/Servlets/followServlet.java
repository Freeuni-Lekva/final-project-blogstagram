package org.blogstagram.followSystem.Servlets;


import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.NotLoggedInException;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.followSystem.Validators.FollowRequestValidator;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.followSystem.api.StatusCodes;
import org.json.JSONObject;


import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;


/*
    check if user is logged in check to user if account exists in data base, we have userName and id
 */



@WebServlet("/followSystem")
public class followServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");
        UserDao userDao = (UserDao) context.getAttribute("UserDAO");
        SqlFollowDao followSystem = (SqlFollowDao) context.getAttribute("SqlFollowDao");
        String toIdStr = request.getParameter("to_id");
        String fromIdStr = (String) request.getSession().getAttribute("from_id");
        FollowRequestValidator validator = new FollowRequestValidator(fromIdStr, toIdStr);
        JSONObject responseJson = new JSONObject();
        int statusCode = 0;
        try {
            if(validator.isFromIdValid() && validator.isToIdValid()){
                Integer fromId = Integer.parseInt(fromIdStr);
                Integer toId = Integer.parseInt(toIdStr);
                FollowApi followApi = new FollowApi();
                followApi.registerFollowRequestSender(); // should get nottificatinDao.
                followApi.setFollowDao(followSystem);
                if(followApi.alreadyFollowed(fromId, toId)){
                    statusCode = followApi.unfollow(fromId, toId);
                }else{
                    statusCode = followApi.sendFollowRequest(fromId, toId);
                }
            }
        } catch (NotLoggedInException | NullPointerException | NotValidUserIdException | DirectionalFollowNotAdded ex) {
            statusCode = StatusCodes.error;
            responseJson.append("errorMessage", ex);
        } finally {
            responseJson.append("status", statusCode);
            response.getWriter().print(responseJson);
        }
    }


}
