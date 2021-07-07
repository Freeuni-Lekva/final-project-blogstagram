package org.blogstagram.listeners;

import org.apache.commons.dbcp2.BasicDataSource;

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
        Connection connection = (Connection) httpSessionEvent.getSession().getServletContext().getAttribute("dbConnection");

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // nothing to do
    }
}
