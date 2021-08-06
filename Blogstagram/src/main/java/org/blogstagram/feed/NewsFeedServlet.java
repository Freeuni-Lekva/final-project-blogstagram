package org.blogstagram.feed;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/")
public class NewsFeedServlet extends HttpServlet {

    private static final String FEED_JSP_PATH = "/jsp/feed/newsFeed.jsp";

    private SqlBlogDAO getSqlBlogDAO(HttpServletRequest req){
        SqlBlogDAO blogDAO = (SqlBlogDAO) req.getSession().getAttribute("blogDao");
        return blogDAO;
    }
    private static Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection) req.getServletContext().getAttribute("dbConnection");
        return connection;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Integer currentUserID = (Integer)req.getSession().getAttribute("currentUserID");
        Connection connection = getConnectionFromContext(req);
        SqlBlogDAO blogDAO = getSqlBlogDAO(req);

        NewsFeedAPI newsFeedAPI = new NewsFeedAPI(currentUserID,blogDAO,connection);
        try {
            List<Blog> blogs = newsFeedAPI.getNewsFeed();
            req.setAttribute("Blogs",blogs);
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (DatabaseError databaseError) {
            databaseError.printStackTrace();
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }


        req.getRequestDispatcher(FEED_JSP_PATH).forward(req,res);
    }
}
