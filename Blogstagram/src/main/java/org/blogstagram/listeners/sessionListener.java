package org.blogstagram.listeners;

import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.dao.UserDAO;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class sessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        // add Dao objects
        Connection dbConnection = (Connection) httpSessionEvent.getSession().getServletContext().getAttribute("dbConnection");
        /*
            Add DAO Objects
         */
        UserDAO userDAO = new UserDAO(dbConnection);
        httpSessionEvent.getSession().setAttribute("UserDAO",userDAO);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // nothing to do
    }
}