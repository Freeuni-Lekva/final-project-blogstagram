package org.blogstagram.currentUser;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/current/user/id")
public class CurrentUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Integer currentUserID = (Integer)req.getSession().getAttribute("currentUserID");
        Gson gson = new Gson();
        res.getWriter().print(gson.toJson(currentUserID));
    }
}
