package org.blogstagram.dao;

import org.blogstagram.blogs.edit.EditBlog;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.DirectedFollow;
import org.blogstagram.models.User;
import org.blogstagram.sql.FollowQueries;
import org.blogstagram.sql.SqlQueries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SqlFollowDao implements FollowDao {
    private final SqlQueries queries;
    private final Connection connection;
    private UserDAO userDao;
    public static final int TEST = 0;
    public static final int REAL = 1;

    public void setUserDao(UserDAO userDao) {
        if(userDao == null) throw new NullPointerException("User Dao object can't be null.");
        this.userDao = userDao;
    }

    public SqlFollowDao(Connection connection, int usePurpose){
        if(connection == null)
            throw new NullPointerException("Connection can't be null");
        if(usePurpose != TEST && usePurpose != REAL){
            throw new IllegalArgumentException("use purpose must be TEST OR REAL implementation.");
        }
        this.connection = connection;
        queries = new FollowQueries(usePurpose);
    }




     /*
        Checks whether user with from id follows user with to id.
     */

    @Override
    public boolean doesConnectionExist(DirectedFollow dFollow) throws DatabaseError {
        if(dFollow == null) throw new  NullPointerException("Follow Object can't be null.");
        List <String> select = new ArrayList<>();
        List <String> where = new ArrayList<>();
        select.add("id");
        where.add("from_user_id");
        where.add("to_user_id");
        try {
            String query = queries.getSelectQuery(select, where);
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, dFollow.getFromId());
            stm.setInt(2, dFollow.getToId());
            ResultSet res = stm.executeQuery();
            boolean result = res.next();
            stm.close();
            return result;
        } catch (InvalidSQLQueryException | SQLException e) {
            throw new DatabaseError("Could't connect to database.");
        }
    }




    /*
        Function deletes row from table, with current from and to id.
     */

    @Override
    public void deleteFollow(DirectedFollow dFollow) throws DatabaseError {
        if(dFollow == null) throw new NullPointerException("Follow Object can't be null");
        List <String> where = new ArrayList<>();
        where.add("from_user_id");
        where.add("to_user_id");
        try {
            String query = queries.getDeleteQuery(where);
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, dFollow.getFromId());
            stm.setInt(2, dFollow.getToId());
            int changedRows = stm.executeUpdate();
            if(changedRows != 1){
                throw new DatabaseError("Affected rows must be 1");
            }
        } catch (InvalidSQLQueryException | SQLException e) {
            throw new DatabaseError("Couldn't connect to database");
        }
    }


    /*
        Function returns all followers of current user.
     */
    @Override
    public List<User> selectAllFollowers(Integer id) throws DatabaseError {
        if(id == null) throw new NullPointerException("User Id can't be null.");
        List <String> select = new ArrayList<>();
        List <String> where = new ArrayList<>();
        select.add("id");
        select.add("from_user_id");
        select.add("to_user_id");
        select.add("created_at");
        where.add("to_user_id");
        List <User> results = new ArrayList<>();
        try {
            String query = queries.getSelectQuery(select, where);
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, id);
            ResultSet res = prpStm.executeQuery();
            while (res.next()){
                User user = userDao.getUserByID(res.getInt(2));
                results.add(user);
            }
            prpStm.close();
        } catch (InvalidSQLQueryException | SQLException e) {
            throw new DatabaseError("Couldn't connect to database");
        }
        return results;
    }


    /*
        Function returns list of users who follow current user.
     */
    @Override
    public List <User> selectAllFollowings(Integer id) throws DatabaseError {
        if(id == null) throw new NullPointerException("User id can't be null");
        List <String> select = new ArrayList<>();
        List <String> where = new ArrayList<>();
        select.add("id");
        select.add("from_user_id");
        select.add("to_user_id");
        select.add("created_at");
        where.add("from_user_id");
        List <User> results = new ArrayList<>();
        try{
            String query = queries.getSelectQuery(select, where);
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet followings = stm.executeQuery();
            while(followings.next()){
                User user = userDao.getUserByID(followings.getInt(3)); //get user by id
                results.add(user);
            }
        } catch (InvalidSQLQueryException | SQLException e) {
            throw new DatabaseError("Couldn't connect to database");
        }
        return results;
    }




    /*
        Function adds directed follow in database, with current from and to user ids.
     */
    @Override
    public void addDirectedFollow(DirectedFollow dFollow) throws DirectionalFollowNotAdded, DatabaseError {
        if(dFollow == null) throw new NullPointerException("User id can't be null");
        List <String> insertFields = new ArrayList<>();
        insertFields.add("from_user_id");
        insertFields.add("to_user_id");
        try {
            String query = queries.getInsertQuery(insertFields, 1);
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, dFollow.getFromId());
            stm.setInt(2, dFollow.getToId());
            int addedRows =  stm.executeUpdate();
            stm.close();
            if(addedRows != 1) throw new DirectionalFollowNotAdded("Directional follow is not added.");
        } catch (InvalidSQLQueryException | SQLException e) {
            throw new DatabaseError("Couldn't connect to database");
        }

    }

}