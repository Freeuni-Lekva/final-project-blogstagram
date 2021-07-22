package org.blogstagram.followSystem.Servlets;

import org.blogstagram.validators.GetUserConnectionsValidator;
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



    private FollowApi initializeFollowApi(FollowDao followDao, UserDAO userDAO){
        FollowApi api = new FollowApi();
        api.setFollowDao(followDao);
        api.setUserDao(userDAO);
       // api.registerFollowRequestSender(null); // notifications
        return api;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //delete
        request.getSession().setAttribute("currentUserId", "2");

        String toIdStr =  request.getParameter("to_id");
        String fromIdStr = (String) request.getSession().getAttribute("currentUserId");
        String requestType = request.getParameter("requestType");
        FollowDao followDao = (SqlFollowDao) request.getSession().getAttribute("SqlFollowDao");
        UserDAO userDao = (UserDAO) request.getSession().getAttribute("UserDAO");
        JSONObject responseJson = ResponseJson.initResponseJson();
        int statusCode = 0;

        try {
            FollowApi followApi = initializeFollowApi(followDao, userDao);
            GetUserConnectionsValidator validator = new GetUserConnectionsValidator();
            validator.setUserDao(userDao);
            validator.setApi(followApi);
            if(validator.validateRequest(fromIdStr, toIdStr)){
                Integer toId = Integer.parseInt(toIdStr);
                List <User> responseLst;

                if(requestType.equals(GETFOLLOWERS)){
                    responseLst = followApi.getAllFollowers(toId);
                    statusCode = StatusCodes.gotAllFollowers;
                } else if(requestType.equals(GETFOLLOWINGS)){
                    responseLst = followApi.getAllFollowing(toId);
                    statusCode = StatusCodes.gotAllFollowings;
                } else {
                    throw new IllegalAccessException("Illegal Request.");
                }
                responseJson.append("responseList", responseLst);
            }
        } catch (NotValidUserIdException | DatabaseError | IllegalAccessException e) {
            statusCode = StatusCodes.error;
            responseJson.append("responseList", null);
            responseJson.append("errorMessage", e.toString());
        } finally {
            responseJson.append("status", statusCode);
            response.getWriter().print(responseJson);
        }
    }
}
