package org.blogstagram.listeners;


import org.blogstagram.dao.CommentDAO;
import org.blogstagram.dao.SqlFollowDao;
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
        UserDAO userDao = new UserDAO(dbConnection);
<<<<<<< HEAD
        SqlFollowDao followDao = new SqlFollowDao(dbConnection,SqlFollowDao.REAL);
=======
        SqlFollowDao followDao = new SqlFollowDao(dbConnection, SqlFollowDao.REAL);
>>>>>>> 5f8c9e2a01d853fa5e5db93908c211f32d8e3b48
        followDao.setUserDao(userDao);
        // set follow request sender to followDao. when implemented
        httpSessionEvent.getSession().setAttribute("SqlFollowDao", followDao);
        httpSessionEvent.getSession().setAttribute("UserDAO", userDao);
        CommentDAO commentDAO = new CommentDAO(dbConnection);
        httpSessionEvent.getSession().getServletContext().setAttribute("CommentDAO", commentDAO);

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // nothing to do!!
    }
}
