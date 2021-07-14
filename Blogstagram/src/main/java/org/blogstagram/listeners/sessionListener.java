package org.blogstagram.listeners;

import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;


@WebListener
public class sessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Connection dbConnection = (Connection) httpSessionEvent.getSession().getServletContext().getAttribute("dbConnection");
        UserDAO userDao = new UserDAO(dbConnection);
        SqlFollowDao followDao = new SqlFollowDao(dbConnection);
        followDao.setUserDao(userDao);
        // set follow request sender to followDao. when implemented
        httpSessionEvent.getSession().setAttribute("SqlFollowDao", followDao);
        httpSessionEvent.getSession().setAttribute("userDao", userDao);

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // nothing to do !!
    }
}
