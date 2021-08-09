package org.blogstagram.listeners;


import org.blogstagram.dao.*;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;


@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Connection dbConnection = (Connection) httpSessionEvent.getSession().getServletContext().getAttribute("dbConnection");
        UserDAO userDao = new UserDAO(dbConnection);
        SqlFollowDao followDao = new SqlFollowDao(dbConnection, SqlFollowDao.REAL);
        CommentDAO commentDAO = new CommentDAO(dbConnection);
        SqlBlogDAO blogDAO = new SqlBlogDAO(dbConnection, userDao, SqlBlogDAO.REAL, commentDAO);
        followDao.setUserDao(userDao);
        // set follow request sender to followDao. when implemented
        httpSessionEvent.getSession().setAttribute("SqlFollowDao", followDao);
        httpSessionEvent.getSession().setAttribute("blogDao", blogDAO);
        httpSessionEvent.getSession().setAttribute("UserDAO", userDao);
        httpSessionEvent.getSession().getServletContext().setAttribute("CommentDAO", commentDAO);
        BlogLikeDao blogLikeDao = new BlogLikeDao(dbConnection);
        httpSessionEvent.getSession().setAttribute("BlogLikeDao", blogLikeDao);

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // nothing to do!!
    }
}
