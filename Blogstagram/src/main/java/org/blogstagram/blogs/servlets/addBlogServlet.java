package org.blogstagram.blogs.servlets;

import com.google.gson.Gson;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.io.IOException;

@WebServlet("/addBlog")
public class addBlogServlet extends HttpServlet {

    private HttpSession getSession(HttpServletRequest request){ return request.getSession(); }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = getSession(request);
        Integer currentUserId = (Integer) session.getAttribute("currentUserId");
        if(currentUserId != null) {
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String moderatorsList = request.getParameter("moderators");
            Gson gson = new Gson();
            Book book = gson.fromJson(moderatorsList, Book.class);
            System.out.println(book);
        }   else{
            response.sendError(response.SC_NOT_FOUND);
        }

    }
}
