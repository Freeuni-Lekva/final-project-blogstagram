package org.blogstagram.followSystem.Servlets;


import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.NotLoggedInException;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.Validators.FollowRequestValidator;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.followSystem.api.ResponseJson;
import org.blogstagram.followSystem.api.StatusCodes;
import org.json.JSONObject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    check if user is logged in check to user if account exists in data base, we have userName and id
 */



@WebServlet("/followSystem")
public class followServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // delete
        request.getSession().setAttribute("from_id", "1");


        ServletContext context = request.getServletContext();
        SqlFollowDao followSystem = (SqlFollowDao) context.getAttribute("SqlFollowDao");
        String toIdStr = request.getParameter("to_id");
        System.out.println(toIdStr);
        String fromIdStr = (String) request.getSession().getAttribute("from_id");
        System.out.println(fromIdStr);
        FollowRequestValidator validator = new FollowRequestValidator();
        JSONObject responseJson = ResponseJson.initResponseJson();
        int statusCode = 0;
        try {
            if(validator.isLoggedIn(fromIdStr) && validator.isIdValid(toIdStr)){
                Integer fromId = Integer.parseInt(fromIdStr);
                Integer toId = Integer.parseInt(toIdStr);
                FollowApi followApi = new FollowApi();
                followApi.registerFollowRequestSender(null); // should get notifficationDao.
                followApi.setFollowDao(followSystem);
                if(followApi.alreadyFollowed(fromId, toId)){
                    followApi.unfollow(fromId, toId);
                    statusCode = StatusCodes.unfollowed;
                }else{
                    followApi.sendFollowRequest(fromId, toId);
                    statusCode = StatusCodes.requestSent;
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
