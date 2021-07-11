package org.blogstagram.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.IOException;


@WebServlet("/user")
public class UserServlet extends HttpServlet {

    /*
     * URL : /user/identificator
     */
    private String getPathIdentificator(HttpServletRequest req){
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");

        if(pathParts.length > 1){
            return null;
        }
        String identificator = pathParts[1];
        return identificator;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String identificator = getPathIdentificator(req);
        if(identificator == null){
            /*
                Logic
             */
            return;
        }
        boolean isID = identificator.matches("[0-9]+");
        if(isID){
            Integer userID = Integer.parseInt(identificator);
            /*
                Logic
             */
        }
        /*
            Logic if identificator is nickname
         */

    }
}
