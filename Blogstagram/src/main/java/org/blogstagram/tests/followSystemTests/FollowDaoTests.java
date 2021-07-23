package org.blogstagram.tests.followSystemTests;

import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.models.DirectedFollow;
import org.blogstagram.models.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;

public class FollowDaoTests {
    @BeforeClass
    public static void setUp(){
        BasicDataSource source = new BasicDataSource();
        source.setUsername("root");
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb");
        source.setPassword(""); // local passsword
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
    private DirectedFollow directedFollow;
    @Before
    public void init(){
        followDao = new SqlFollowDao(connection, SqlFollowDao.TEST);
        followDao.setUserDao(userDao);
        directedFollow = new DirectedFollow();
        directedFollow.setId(DirectedFollow.NO_ID);
    }

    @Test
    public void testFollowDao1(){
        assertThrows(NullPointerException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                followDao.addDirectedFollow(null);
            }
        });
    }

    @Test
    public void testFollowDao2(){
        directedFollow.setFromId(1);
        directedFollow.setToId(3);
        try {
            assertFalse(followDao.doesConnectionExist(directedFollow));
        } catch (DatabaseError directionalFollowNotAdded) {
            directionalFollowNotAdded.printStackTrace();
        }
    }

    @Test
    public void testFollowDao3() {
        try {
            directedFollow.setFromId(2);
            directedFollow.setToId(1);
            followDao.addDirectedFollow(directedFollow);
            assertTrue(followDao.doesConnectionExist(directedFollow));
        }  catch(DatabaseError | DirectionalFollowNotAdded databaseError){
                databaseError.printStackTrace();
        }
    }

    @Test
    public void testFollowDao4(){
        assertThrows(NullPointerException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                followDao.doesConnectionExist(null);
            }
        });
    }

    @Test
    public void testFollowDao5(){
        directedFollow.setFromId(3);
        directedFollow.setToId(5);
        try {
            followDao.addDirectedFollow(directedFollow);
            directedFollow.setToId(3);
            directedFollow.setFromId(5);
            followDao.addDirectedFollow(directedFollow);
            assertTrue(followDao.doesConnectionExist(directedFollow));
            directedFollow.setToId(5);
            directedFollow.setFromId(3);
            assertTrue(followDao.doesConnectionExist(directedFollow));
            directedFollow.setFromId(1);
            directedFollow.setToId(6);
            assertFalse(followDao.doesConnectionExist(directedFollow));
        } catch (DirectionalFollowNotAdded | DatabaseError directionalFollowNotAdded) {
            directionalFollowNotAdded.printStackTrace();
        }
    }

    @Test
    public void testFollowDao6(){
        directedFollow.setFromId(1);
        directedFollow.setToId(3);
        try {
            followDao.addDirectedFollow(directedFollow);
            assertTrue(followDao.doesConnectionExist(directedFollow));
            followDao.deleteFollow(directedFollow);
            assertFalse(followDao.doesConnectionExist(directedFollow));
        } catch (DirectionalFollowNotAdded | DatabaseError directionalFollowNotAdded) {
            directionalFollowNotAdded.printStackTrace();
        }
    }




    @Test
    public void testFollowDao7(){
        int fromId = 6;
        for(int k = 1; k < 101; k++){
            if(k == fromId) continue;
            try {
                directedFollow.setFromId(fromId);
                directedFollow.setToId(k);
                assertFalse(followDao.doesConnectionExist(directedFollow));
                followDao.addDirectedFollow(directedFollow);
                assertTrue(followDao.doesConnectionExist(directedFollow));
                followDao.deleteFollow(directedFollow);
                assertFalse(followDao.doesConnectionExist(directedFollow));
            } catch (DatabaseError | DirectionalFollowNotAdded databaseError) {
                databaseError.printStackTrace();
            }

        }
    }

    @Test
    public void testFollowDao8(){
        int fromId = 20;
        int toId = 21;
        directedFollow.setToId(toId);
        directedFollow.setFromId(fromId);
        assertThrows(DatabaseError.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                followDao.deleteFollow(directedFollow);
            }
        });
    }

    @Test
    public void testFollowDap9(){
        assertThrows(NullPointerException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                followDao.deleteFollow(null);
            }
        });
    }

    @Test
    public void testFollowDao10(){
        directedFollow.setFromId(20);
        for(int id = 1; id <= 10; id++){
            directedFollow.setToId(id);
            try {
                assertFalse(followDao.doesConnectionExist(directedFollow));
                followDao.addDirectedFollow(directedFollow);
                assertTrue(followDao.doesConnectionExist(directedFollow));
            } catch (DirectionalFollowNotAdded | DatabaseError directionalFollowNotAdded) {
                directionalFollowNotAdded.printStackTrace();
            }
        }

        List<User> expected = new ArrayList<>();
        for(int k = 1; k < 11; k++){
            try {
                expected.add(userDao.getUserByID(k));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

        try {
            List <User> actual = followDao.selectAllFollowings(20);
            assertEquals(actual.size(), expected.size());
            for(int k = 1; k <= 10; k++) {
                if(expected.get(k - 1) == null){
                    assertNull(actual.get(k - 1));
                }
                else assertEquals(expected.get(k - 1).getId(), actual.get(k - 1).getId());
            }
        } catch (DatabaseError databaseError) {
            databaseError.printStackTrace();
        }

        for(int id = 1; id <= 10; id++){
            directedFollow.setToId(id);
            try {
                followDao.deleteFollow(directedFollow);
            } catch (DatabaseError databaseError) {
                databaseError.printStackTrace();
            }
        }


    }

    @Test
    public void testFollowDao11(){
        directedFollow.setToId(20);
        for(int id = 1; id <= 10; id++){
            directedFollow.setFromId(id);
            try {
                followDao.addDirectedFollow(directedFollow);
            } catch (DirectionalFollowNotAdded | DatabaseError directionalFollowNotAdded) {
                directionalFollowNotAdded.printStackTrace();
            }
        }

        List<User> expected = new ArrayList<>();
        for(int k = 1; k < 11; k++) {
            try {
                expected.add(userDao.getUserByID(k));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

        List <User> actual = null;
        try {
            actual = followDao.selectAllFollowers(20);
        } catch (DatabaseError databaseError) {
            databaseError.printStackTrace();
        }
        assert actual != null;
        assertEquals(expected.size(), actual.size());
        for(int id = 1; id <= 10; id++){
            if(expected.get(id - 1) != null) assertEquals(expected.get(id - 1).getId(), actual.get(id - 1).getId());
            else assertNull(actual.get(id - 1));
        }

        for(int id = 1; id <= 10; id++){
            directedFollow.setFromId(id);
            try {
                followDao.deleteFollow(directedFollow);
            } catch (DatabaseError databaseError) {
                databaseError.printStackTrace();
            }
        }
    }


}
