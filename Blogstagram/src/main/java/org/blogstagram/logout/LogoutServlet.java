package org.blogstagram.logout;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private void removeCurrentUserSession(HttpServletRequest req) {
        req.getSession().setAttribute("currentUserID",null);
        req.getSession().setAttribute("currentUserNickname",null);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        removeCurrentUserSession(req);
        res.sendRedirect("/register");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        removeCurrentUserSession(req);
        res.sendRedirect("/register");
    }
}
