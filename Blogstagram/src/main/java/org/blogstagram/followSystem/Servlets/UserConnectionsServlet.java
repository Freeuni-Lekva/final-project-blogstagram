package org.blogstagram.followSystem.Servlets;

import org.blogstagram.Validators.FollowRequestValidator;
import org.blogstagram.Validators.UserIdValidator;
import org.blogstagram.dao.FollowDao;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.followSystem.api.ResponseJson;
import org.blogstagram.followSystem.api.StatusCodes;
import org.blogstagram.models.User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/UserConnections")
public class UserConnectionsServlet extends HttpServlet {
    private static final String GETFOLLOWERS = "Followers";
    private static final String GETFOLLOWINGS = "Followings";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = (String) request.getSession().getAttribute("to_id");
        Integer id = Integer.parseInt(idStr);
        FollowDao followDao = (SqlFollowDao) request.getServletContext().getAttribute("SqlFollowDao");
        UserDAO userDao = (UserDAO) request.getServletContext().getAttribute("UserDao");
        JSONObject responseJson = ResponseJson.initResponseJson();
        UserIdValidator validator = new UserIdValidator();
        validator.setUserDao(userDao);
        String requestType = (String) request.getSession().getAttribute("requestType");
        try {
            if(validator.validate(id)){
                FollowApi api = new FollowApi();
                api.setUserDao(userDao);
                api.registerFollowRequestSender(null); // notifications
                api.setFollowDao(followDao);
                List <User> responseLst = null;
                if(requestType.equals(GETFOLLOWERS)){
                    responseLst = api.getAllFollowers(id);
                    responseJson.append("status", StatusCodes.gotAllFollowers);
                } else if(requestType.equals(GETFOLLOWINGS)){
                    responseLst = api.getAllFollowing(id);
                    responseJson.append("status", StatusCodes.gotAllFollowings);
                } else {
                    throw new IllegalAccessException("Illegal Request.");
                }
                responseJson.append("responseList", responseLst);
            }
        } catch (NotValidUserIdException | DatabaseError | IllegalAccessException e) {
            responseJson.append("status", StatusCodes.error);
            responseJson.append("errorMessage", e.toString());
        } finally {
            response.getWriter().print(responseJson);
        }
    }
}
