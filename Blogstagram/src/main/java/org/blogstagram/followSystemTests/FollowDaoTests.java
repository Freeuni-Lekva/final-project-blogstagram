package org.blogstagram.followSystemTests;

import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.dao.FollowDao;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class FollowDaoTests {
    @BeforeClass
    public static void setUp(){
        BasicDataSource source = new BasicDataSource();
        source.setUsername("root");
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb");
        source.setPassword("Arqimede123@"); // local passsword
        try {
            connection = source.getConnection();
            userDao = new UserDAO(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static Connection connection;
    private static UserDAO userDao;
    private SqlFollowDao followDao;
    @Before
    public void init(){
        followDao = new SqlFollowDao(connection);
        followDao.setUserDao(userDao);
    }

    @Test
    public void testFollowDao1(){

    }


}
