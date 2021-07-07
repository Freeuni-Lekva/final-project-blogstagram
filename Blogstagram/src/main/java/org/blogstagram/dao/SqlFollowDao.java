package org.blogstagram.dao;

import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.listeners.followNotificationSender;
import org.blogstagram.models.DirectedFollow;
import org.blogstagram.sql.FollowQueries;
import org.blogstagram.sql.SqlQueries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SqlFollowDao implements FollowDao {
    private SqlQueries queries;
    private Connection connection;
    public SqlFollowDao(Connection connection){
        if(connection == null)
            return; // return error
        this.connection = connection;
        queries = new FollowQueries();
    }




     /*
        Checks whether user with from id follows user with to id.
     */

    @Override
    public boolean doesConnectionExist(DirectedFollow dFollow){
        if(dFollow == null) throw new  NullPointerException("Follow Object can't be null.");
        List <String> select = new ArrayList<>();
        List <String> where = new ArrayList<>();
        select.add("id");
        where.add("from_user_id");
        where.add("to_user_id");
        try {
            String query = queries.getSelectQuery(select, where);
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(dFollow.getFromId(), 1);
            stm.setInt(dFollow.getToId(), 2);
            ResultSet res = stm.executeQuery();
            stm.close();
            return res.next();
        } catch (InvalidSQLQueryException | SQLException e) {
            // think what to do with errors.
        }
        return false;
    }




    /*
        Function deletes row from table, with current from and to id.
     */

    @Override
    public void deleteFollow(DirectedFollow dFollow){
        if(dFollow == null) throw new NullPointerException("Follow Object can't be null");
        List <String> where = new ArrayList<>();
        where.add("from_used_id");
        where.add("to_user_id");
        try {
            String query = queries.getDeleteQuery(where);
            PreparedStatement stm = connection.prepareStatement(query);
            int changedRows = stm.executeUpdate();
            if(changedRows != 1){
                // add errors implement logic
            }
        } catch (InvalidSQLQueryException | SQLException e) {
            e.printStackTrace();
        }
    }


    /*
        Function returns all followers of current user.
     */
    @Override
    public List<DirectedFollow> selectAllFollowers(Integer id){
        if(id == null) throw new NullPointerException("User Id can't be null.");
        List <String> select = new ArrayList<>();
        List <String> where = new ArrayList<>();
        select.add("id");
        select.add("from_user_id");
        select.add("to_user_id");
        select.add("created_at");
        where.add("to_user_id");
        List <DirectedFollow> results = new ArrayList<>();
        try {
            String query = queries.getSelectQuery(select, where);
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, id);
            ResultSet res = prpStm.executeQuery(query);
            while (res.next()){
                DirectedFollow follow = new DirectedFollow();
                follow.setId(res.getInt(1));
                follow.setFromId(res.getInt(2));
                follow.setToId(res.getInt(3));
                follow.setCreatedAt(res.getDate(4));
                results.add(follow);
            }
            prpStm.close();
        } catch (InvalidSQLQueryException | SQLException e) {
            // implement logic
        }
        return results;
    }

    /*
        Function returns list of users who follow current user.
     */
    @Override
    public List<DirectedFollow> selectAllFollowings(Integer id){
        if(id == null) throw new NullPointerException("User id can't be null");
        List <String> select = new ArrayList<>();
        List <String> where = new ArrayList<>();
        select.add("id");
        select.add("from_user_id");
        select.add("to_user_id");
        select.add("created_at");
        where.add("from_user_id");
        List <DirectedFollow> results = new ArrayList<>();
        try{
            String query = queries.getSelectQuery(select, where);
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet followings = stm.executeQuery();
            while(followings.next()){
                DirectedFollow follow = new DirectedFollow();
                follow.setId(followings.getInt(1));
                follow.setFromId(followings.getInt(2));
                follow.setToId(followings.getInt(3));
                follow.setCreatedAt(followings.getDate(4));
                results.add(follow);
            }
        } catch (InvalidSQLQueryException | SQLException e) {

        }
        return results;
    }




    /*
        Function adds directed follow in database, with current from and to user ids.
     */
    @Override
    public void addDirectedFollow(DirectedFollow dFollow) throws DirectionalFollowNotAdded {
        if(dFollow == null) throw new NullPointerException("User id can't be null");
        List <String> insertFields = new ArrayList<>();
        List <String> values = new ArrayList<>();
        insertFields.add("from_user_id");
        insertFields.add("to_user_id");
        try {
            String query = queries.getInsertQuery(insertFields);
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, dFollow.getFromId());
            stm.setInt(2, dFollow.getToId());
            int addedRows =  stm.executeUpdate();
            stm.close();
            if(addedRows == 0) throw new DirectionalFollowNotAdded("Directional follow is not added.");
        } catch (InvalidSQLQueryException | SQLException e) {
           //implement
        }

    }


}
