package org.blogstagram.listeners;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;

import org.blogstagram.dao.SqlFollowDao;

@WebListener
public class contextListener implements ServletContextListener {
    private BasicDataSource source;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        source = new BasicDataSource();
        source.setUsername("root");
        source.setPassword(""); // local password
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb");
        try {
            Connection dbConnection = source.getConnection();
            servletContextEvent.getServletContext().setAttribute("dbConnection", dbConnection);
            SqlFollowDao followDao = new SqlFollowDao(dbConnection);
            followDao.setUserDao(userDao);
            // set follow request sender to followDao.
            servletContextEvent.getServletContext().setAttribute("SqlFollowDao", followDao);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Connection dbConnection = (Connection) servletContextEvent.getServletContext().getAttribute("dbConnection");
        try {
            dbConnection.close();
            source.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
