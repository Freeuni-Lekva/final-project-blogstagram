package org.blogstagram.listeners;

import org.blogstagram.dao.CommentDAO;
import org.blogstagram.dao.UserDAO;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Connection dbConnection = (Connection) httpSessionEvent.getSession().getServletContext().getAttribute("dbConnection");
        UserDAO userDao = (UserDAO) httpSessionEvent.getSession().getServletContext().getAttribute("UserDao");
//        SqlFollowDao followDao = new SqlFollowDao(dbConnection);
//        followDao.setUserDao(userDAO);
//        httpSessionEvent.getSession().setAttribute("followDao", followDao);
        CommentDAO commentDAO = new CommentDAO(dbConnection);
        httpSessionEvent.getSession().getServletContext().setAttribute("CommentDAO", commentDAO);

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //
    }
}
