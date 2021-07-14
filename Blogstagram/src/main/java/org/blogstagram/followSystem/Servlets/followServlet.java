package org.blogstagram.followSystem.Servlets;


import org.blogstagram.dao.FollowDao;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.NotLoggedInException;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.validators.FollowRequestValidator;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.followSystem.api.ResponseJson;
import org.blogstagram.followSystem.api.StatusCodes;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
    check if user is logged in check to user if account exists in data base, we have userName and id
 */



@WebServlet("/followSystem")
public class followServlet extends HttpServlet {
    private FollowApi initializeFollowApi(FollowDao followDao, UserDAO userDAO){
        FollowApi api = new FollowApi();
        api.setFollowDao(followDao);
        api.setUserDao(userDAO);
        //api.registerFollowRequestSender(null); // notifications
        return api;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // delete
        request.getSession().setAttribute("from_id", "1");

        // check how to secure ajax requests.

        HttpSession session = request.getSession();
        SqlFollowDao followSystem = (SqlFollowDao) session.getAttribute("SqlFollowDao");
        UserDAO userDao = (UserDAO) request.getSession().getAttribute("UserDAO");
        String toIdStr = request.getParameter("to_id");
        String fromIdStr = (String) request.getSession().getAttribute("from_id");
        FollowRequestValidator validator = new FollowRequestValidator();
        validator.setUserDao(userDao);
        JSONObject responseJson = ResponseJson.initResponseJson();
        int statusCode = 0;
        try {
            if(validator.isLoggedIn(fromIdStr) && validator.isIdValid(toIdStr)){
                Integer fromId = Integer.parseInt(fromIdStr);
                Integer toId = Integer.parseInt(toIdStr);
                FollowApi followApi = initializeFollowApi(followSystem, userDao);
                int condition = followApi.alreadyFollowed(fromId, toId);

                if(condition == StatusCodes.followed){
                    statusCode = followApi.unfollow(fromId, toId);
                }else if(condition == StatusCodes.notFollowed){

                    statusCode = followApi.sendFollowRequest(fromId, toId);
                }
            }
        } catch (NotLoggedInException | NullPointerException | NotValidUserIdException | DirectionalFollowNotAdded | DatabaseError ex) {
            statusCode = StatusCodes.error;
            responseJson.append("errorMessage", ex.toString());
        } finally {
            responseJson.append("status", statusCode);
            response.getWriter().print(responseJson);
        }
    }


}
