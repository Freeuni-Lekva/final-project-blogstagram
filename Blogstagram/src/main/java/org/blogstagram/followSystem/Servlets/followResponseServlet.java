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
        String toIdStr = (String) request.getSession().getAttribute("to_id");
        String fromIdStr = (String) request.getAttribute("from_id");
        Integer toId = Integer.parseInt(toIdStr);
        Integer fromId = Integer.parseInt(fromIdStr);
        UserDAO userDao = (UserDAO) request.getSession().getAttribute("UserDAO");
        FollowRequestValidator validator = new FollowRequestValidator();
        validator.setUserDao(userDao);
        FollowDao followDao = (SqlFollowDao) request.getServletContext().getAttribute("SqlFollowDao");
        JSONObject responseJson = ResponseJson.initResponseJson();
        int statusCode = 0;
        try {
            if(validator.isLoggedIn(toIdStr) && validator.isIdValid(fromIdStr)){
                if(request.getParameter("Accept") != null){
                    FollowApi api = initializeFollowApi(followDao, userDao);
                    statusCode = api.acceptFollowRequest(fromId, toId);
                }else if(request.getParameter("Decline") != null){
                    // decline  so it must be forwarded to notification api.
                    statusCode = StatusCodes.requestDeclined;
                }else{
                    throw new IllegalAccessException("Illegal request.");
                }

            }
        } catch (NotValidUserIdException | DirectionalFollowNotAdded | DatabaseError |
                NotLoggedInException | IllegalAccessException  e) {
            statusCode = StatusCodes.error;
            responseJson.append("errorMessage", e.toString());
        } finally {
            responseJson.append("status", statusCode);
            response.getWriter().print(responseJson);
        }
    }
}
