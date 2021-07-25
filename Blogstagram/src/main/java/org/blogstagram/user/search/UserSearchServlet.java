package org.blogstagram.user.search;

import com.google.gson.Gson;
import org.blogstagram.models.SearchedUser;
import org.blogstagram.pairs.StringPair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/search/user")
public class UserSearchServlet extends HttpServlet {

    private Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection) req.getServletContext().getAttribute("dbConnection");
        return connection;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String query = req.getParameter("query");
        UserSearchAPI userSearchAPI = new UserSearchAPI(getConnectionFromContext(req));

        Gson gson = new Gson();
        try {
            List<SearchedUser> users = userSearchAPI.searchUsers(query);
            res.getWriter().println(gson.toJson(users));
        } catch (SQLException exception) {
            exception.printStackTrace();
            res.getWriter().println("{ error: something went wrong }");
        }
    }
}
