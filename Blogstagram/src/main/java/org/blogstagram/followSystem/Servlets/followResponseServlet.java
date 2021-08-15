package org.blogstagram.followSystem.Servlets;

import org.blogstagram.validators.FollowRequestValidator;
import org.blogstagram.dao.FollowDao;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.NotLoggedInException;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.followSystem.api.ResponseJson;
import org.blogstagram.followSystem.api.StatusCodes;
import org.json.JSONObject;
import org.blogstagram.followSystem.api.FollowApi;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/followResponse")
public class followResponseServlet extends HttpServlet {

    private FollowApi initializeFollowApi(FollowDao followDao, UserDAO userDAO){
        FollowApi api = new FollowApi();
        api.setFollowDao(followDao);
        api.setUserDao(userDAO);
      //  api.registerFollowRequestSender(null); // notifications
        return api;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //implement logic if response is accept then write follow request to database.
        // add logic if notification is sent then add follow.  ............................. important
        // add logic to validate request.
        Integer toId = (Integer) request.getSession().getAttribute("currentUserID");
        String toIdStr = String.valueOf(toId);
        String fromIdStr = (String) request.getAttribute("from_id");
        Integer fromId = Integer.parseInt(fromIdStr);
        UserDAO userDao = (UserDAO) request.getSession().getAttribute("UserDAO");
        FollowRequestValidator validator = new FollowRequestValidator();
        validator.setUserDao(userDao);
        FollowDao followDao = (SqlFollowDao) request.getSession().getAttribute("SqlFollowDao");
        JSONObject responseJson = ResponseJson.initResponseJson();
        int statusCode = 0;
        String type = request.getParameter("Type");
        try {
            if(validator.isLoggedIn(toIdStr) && validator.isIdValid(fromIdStr)){
                if(type.equals("Accept")){
                    FollowApi api = initializeFollowApi(followDao, userDao);
                    statusCode = api.acceptFollowRequest(fromId, toId);
                }else if(type.equals("Decline")){
                    // decline  so it must be forwarded to notification api.
                    statusCode = StatusCodes.requestDeclined;
                }else{
                    response.sendError(response.SC_BAD_REQUEST);
                }

            }
        } catch (NotValidUserIdException | DirectionalFollowNotAdded | DatabaseError |
                NotLoggedInException e) {
            statusCode = StatusCodes.error;
            responseJson.append("errorMessage", e.toString());
        } finally {
            responseJson.append("status", statusCode);
            response.getWriter().print(responseJson);
        }
    }
}
