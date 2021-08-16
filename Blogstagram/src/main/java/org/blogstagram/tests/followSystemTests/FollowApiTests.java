package org.blogstagram.tests.followSystemTests;

import org.apache.commons.dbcp2.BasicDataSource;
import org.blogstagram.dao.FollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.followSystem.api.StatusCodes;
import org.blogstagram.models.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;

import org.mockito.junit.MockitoJUnitRunner;
import org.blogstagram.followSystem.api.FollowApi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FollowApiTests {


    // add accept follow request tests

    @BeforeClass
    public static void setUp() throws SQLException {
        BasicDataSource source = new BasicDataSource();
        source.setUsername("root");
        source.setUrl("jdbc:mysql://localhost:3306/blogstagramdb");
        source.setPassword(""); // local passsword
        connection = source.getConnection();
    }

    private static Connection connection;


    private FollowDao followDao;



    private FollowApi followApi;

    private UserDAO UserDao;


    @Before
    public void init(){
        followApi = new FollowApi();
        followDao = new FollowDaoDummy();
        UserDao = new UserDAO(connection);
        followApi.setUserDao(UserDao);
        followApi.setFollowDao(followDao);
    }

    @Test
    public void testFollowApi1(){
        try {
            assertNotNull(followDao);
            assertNotNull(UserDao);
            assertNotNull(followApi);
            int res = followApi.sendFollowRequest(1, 2);
            assertEquals(StatusCodes.followed, res);
            res = followApi.sendFollowRequest(1, 4);
            assertEquals(StatusCodes.followed, res);
            List <Integer> ids = Arrays.asList(2, 4);
            List<User> result = followDao.selectAllFollowings(1);
            assertEquals(2, result.size());
            for(int k = 0; k < result.size(); k++){
                assertEquals(result.get(k).getId(), ids.get(k));
            }
        } catch (DatabaseError | DirectionalFollowNotAdded e){
            e.printStackTrace();
        }
    }


    @Test
    public void testFollowApi2(){
        try {
            assertNotNull(followDao);
            assertNotNull(UserDao);
            assertNotNull(followApi);
            int res = followApi.sendFollowRequest(1, 2);
            assertEquals(StatusCodes.followed, res);
            res =  followApi.sendFollowRequest(1, 4);
            assertEquals(StatusCodes.followed,res);
            res = followApi.sendFollowRequest(1, 3);
            assertEquals(StatusCodes.requestSent, res);
            List <Integer> ids = Arrays.asList(2, 4);
            List<User> result = followDao.selectAllFollowings(1);
            assertEquals(2, result.size());
            for(int k = 0; k < result.size(); k++){
                assertEquals(result.get(k).getId(), ids.get(k));
            }
        } catch (DatabaseError | DirectionalFollowNotAdded e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFollowApi3(){
        try {
            assertNotNull(followDao);
            assertNotNull(UserDao);
            assertNotNull(followApi);
            int res =  followApi.sendFollowRequest(1, 2);
            assertEquals(StatusCodes.followed, res);
            res = followApi.sendFollowRequest(1, 4);
            assertEquals(StatusCodes.followed, res);
            res = followApi.sendFollowRequest(1, 3);
            assertEquals(StatusCodes.requestSent, res);
            res = followApi.sendFollowRequest(2, 1);
            assertEquals(StatusCodes.followed, res);
            res = followApi.sendFollowRequest(2, 4);
            assertEquals(StatusCodes.followed, res);
            List <Integer> ids = Arrays.asList(2, 4);
            List<User> result = followDao.selectAllFollowings(1);
            for(int k = 0; k < result.size(); k++){
                assertEquals(result.get(k).getId(), ids.get(k));
            }
            List <User> followers = followDao.selectAllFollowers(1);
            ids = Collections.singletonList(2);
            for(int k = 0; k < followers.size(); k++) assertEquals(ids.get(k), followers.get(k).getId());
            followers = followDao.selectAllFollowers(4);
            ids = Arrays.asList(1, 2);
            for(int k = 0; k < followers.size(); k++) assertEquals(ids.get(k), followers.get(k).getId());
            result = followDao.selectAllFollowings(2);
            ids = Arrays.asList(1, 4);
            for(int k = 0; k < result.size(); k++) assertEquals(result.get(k).getId(), ids.get(k));
        } catch (DatabaseError | DirectionalFollowNotAdded e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFollowApi4(){
        try {
            assertNotNull(followDao);
            assertNotNull(UserDao);
            assertNotNull(followApi);
            int res = followApi.sendFollowRequest(1, 2);
            assertEquals(StatusCodes.followed, res);
            res = followApi.sendFollowRequest(1, 4);
            assertEquals(StatusCodes.followed, res);
            res = followApi.unfollow(1, 2);
            assertEquals(StatusCodes.unfollowed, res);
            List <User> followings = followDao.selectAllFollowings(1);
            List <Integer> ids = Collections.singletonList(4);
            assertEquals(1, followings.size());
            for(int k = 0; k < followings.size(); k++) assertEquals(followings.get(k).getId(), ids.get(k));
            res = followApi.unfollow(1, 4);
            assertEquals(StatusCodes.unfollowed, res);
            followings = followDao.selectAllFollowings(1);
            assertNull(followings);
            List <User> followers = followDao.selectAllFollowers(2);
            assertNull(followers);
            followers = followDao.selectAllFollowers(4);
            assertNull(followers);
        } catch (DatabaseError | DirectionalFollowNotAdded e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFollow5(){
        try {
            assertNotNull(followDao);
            assertNotNull(UserDao);
            assertNotNull(followApi);
            int res = followApi.sendFollowRequest(1, 2);
            assertEquals(StatusCodes.followed, res);
            res = followApi.unfollow(1, 2);
            assertEquals(StatusCodes.unfollowed, res);
            assertThrows(DatabaseError.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    followApi.unfollow(1, 2);
                }
            });
        }  catch (DatabaseError | DirectionalFollowNotAdded  e){
            e.printStackTrace();
        }

    }

    @Test
    public void testFollowApi6(){
        try{
            followApi.sendFollowRequest(1, 2);
            int numFollowings = followApi.getFollowingCount(1);
            assertEquals(1, numFollowings);
            int numFollowers = followApi.getFollowersCount(2);
            assertEquals(1, numFollowers);
            followApi.sendFollowRequest(1, 4);
            numFollowings = followApi.getFollowingCount(1);
            assertEquals(2, numFollowings);
            numFollowers = followApi.getFollowersCount(4);
            assertEquals(1, numFollowers);
            followApi.unfollow(1, 4);
            numFollowings = followApi.getFollowingCount(1);
            followApi.sendFollowRequest(1, 3);
            numFollowings = followApi.getFollowingCount(1);
            assertEquals(1, numFollowings);
        } catch(DatabaseError | DirectionalFollowNotAdded | NotValidUserIdException e){
            e.printStackTrace();
        }
    }


    @Test
    public void testFollowApi7(){
        try{
            followApi.sendFollowRequest(1, 2);
            int res = followApi.alreadyFollowed(1, 2);
            assertEquals(StatusCodes.followed, res);
            res = followApi.alreadyFollowed(2, 1);
            assertEquals(StatusCodes.notFollowed, res);
            res = followApi.sendFollowRequest(2, 1);
            assertEquals(StatusCodes.followed, res);

        }catch(DatabaseError | DirectionalFollowNotAdded | SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFollowApi8(){
        try{
            int res = followApi.sendFollowRequest(1, 4);
            assertEquals(StatusCodes.followed, res);
            res = followApi.sendFollowRequest(1, 2);
            assertEquals(StatusCodes.followed, res);
            res = followApi.sendFollowRequest(2, 1);
            assertEquals(StatusCodes.followed, res);
            res = followApi.sendFollowRequest(4, 1);
            assertEquals(StatusCodes.followed, res);
            res = followApi.alreadyFollowed(1, 4);
            assertEquals(res, StatusCodes.followed);
            res = followApi.alreadyFollowed(4, 1);
            assertEquals(res, StatusCodes.followed);
            int numFollowings = followApi.getFollowingCount(1);
            assertEquals(2, numFollowings);
            List <User> followings = followApi.getAllFollowing(1);
            List <Integer> ids = Arrays.asList(2, 4);
            for(int k = 0; k < followings.size(); k++) assertEquals(followings.get(k).getId(), ids.get(k));
            followings = followApi.getAllFollowing(2);
            ids = Collections.singletonList(1);
            for(int k = 0; k < followings.size(); k++) assertEquals(followings.get(k).getId(), ids.get(k));
            List <User> followers = followApi.getAllFollowers(1);
            ids = Arrays.asList(2, 4);
            for(int k = 0; k < followers.size(); k++) assertEquals(followers.get(k).getId(), ids.get(k));
            res = followApi.sendFollowRequest(2, 4);
            assertEquals(res, StatusCodes.followed);
            res = followApi.sendFollowRequest(4, 2);
            assertEquals(StatusCodes.followed, res);
            followers = followApi.getAllFollowers(4);
            ids = Arrays.asList(1, 2);
            for(int k = 0; k < followers.size(); k++) assertEquals(followers.get(k).getId(), ids.get(k));
            followers = followApi.getAllFollowers(2);
            ids = Arrays.asList(1, 4);
            for(int k = 0; k < followers.size(); k++) assertEquals(followers.get(k).getId(), ids.get(k));
            followApi.unfollow(4, 2);
            ids = Collections.singletonList(1);
            followers = followApi.getAllFollowers(2);
            for(int k = 0; k < followers.size(); k++) assertEquals(followers.get(k).getId(), ids.get(k));
            followings = followApi.getAllFollowing(4);
            ids = Collections.singletonList(1);
            for(int k = 0; k < followings.size(); k++) assertEquals(followings.get(k).getId(), ids.get(k));
        } catch (DatabaseError | DirectionalFollowNotAdded | NotValidUserIdException | SQLException e){
            e.printStackTrace();
        }
    }



}
