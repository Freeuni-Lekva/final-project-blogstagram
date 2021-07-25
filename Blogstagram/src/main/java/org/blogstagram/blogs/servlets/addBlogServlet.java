package org.blogstagram.blogs.servlets;

import com.google.gson.Gson;
import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/addBlog")
public class addBlogServlet extends HttpServlet {

    private HttpSession getSession(HttpServletRequest request){ return request.getSession(); }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute("currentUserId", 1);
        HttpSession session = getSession(request);
        UserDAO userDAO = (UserDAO) session.getAttribute("UserDAO");
        SqlBlogDAO blogDAO = (SqlBlogDAO) session.getAttribute("BlogDao");
        Integer currentUserId = (Integer) session.getAttribute("currentUserId");
        System.out.println(currentUserId);
        if(userDAO == null) System.out.println("fuck");
        if(currentUserId != null) {
            String title = request.getParameter("title");
            String content = request.getParameter("content");

            List <User> moderators = new ArrayList<>();
            String moderatorsList = request.getParameter("moderators");
            final JSONObject object = new JSONObject(moderatorsList);
            final JSONArray moderatorArray = object.getJSONArray("users");
            for(int k = 0; k < moderatorArray.length(); k++){
                final JSONObject moderator = moderatorArray.getJSONObject(k);
                try {
                    moderators.add(userDAO.getUserByID(moderator.getInt("user_id")));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            Blog newBlog = new Blog();
            newBlog.setUser_id(currentUserId);
            newBlog.setTitle(title);
            newBlog.setContent(content);
            newBlog.setBlogModerators(moderators);
            try {
                blogDAO.addBlog(newBlog);
            } catch (InvalidSQLQueryException | DatabaseError e) {

                //implement
                e.printStackTrace();
            }
        }   else{
            response.sendError(response.SC_NOT_FOUND);
        }

    }
}
