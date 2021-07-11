package org.blogstagram.listeners;

import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDao;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;


@WebListener
public class sessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Connection dbConnection = (Connection) httpSessionEvent.getSession().getServletContext().getAttribute("dbConnection");
        UserDao userDao = (UserDao) httpSessionEvent.getSession().getServletContext().getAttribute("UserDao");
        SqlFollowDao followDao = new SqlFollowDao(dbConnection);
        followDao.setUserDao(userDao);
        // set follow request sender to followDao. when implemented
        httpSessionEvent.getSession().setAttribute("followDao", followDao);

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // nothing to do !!
    }
}
