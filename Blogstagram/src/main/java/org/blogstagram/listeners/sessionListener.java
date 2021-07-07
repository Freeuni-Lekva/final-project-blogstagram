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
        BasicDataSource source = new BasicDataSource();
        source.setUsername("root");
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb");
        try {
            Connection dbConnection = source.getConnection();
            /*
                Add DAO Objects
             */
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // nothing to do
    }
}
