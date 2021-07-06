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
    private Connection connection;
    private SqlQueries queries;
    private UserDao userDao;
    private followNotificationSender sender;
    public SqlFollowDao(Connection connection){
        if(connection == null)
            return; // return error
        this.connection = connection;
        queries = new FollowQueries();
    }

    public void setUserDao(UserDao userDao){
        if(userDao == null) return; // error
        this.userDao = userDao;
    }

    public void registerFollowRequestSender(followNotificationSender sender){
        if(sender == null) throw new NullPointerException("Follow request Sender can't be null.");
        this.sender = sender;
    }
    @Override
    public boolean isAlreadyFollowed(Integer fromId, Integer toId){
        if(fromId == null){
            throw new NullPointerException("From id is null.");
        }else if(toId == null){
            throw new NullPointerException("To id is null");
        }
        List <String> select = new ArrayList<>();
        List <String> where = new ArrayList<>();
        select.add("id");
        where.add("from_user_id");
        where.add("to_user_id");
        try {
            String query = queries.getSelectQuery(select, where);
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(fromId, 1);
            stm.setInt(toId, 2);
            ResultSet res = stm.executeQuery();
            stm.close();
            return res.next();
        } catch (InvalidSQLQueryException | SQLException e) {
            // think what to do with errors.
        }
        return false;
    }

    @Override
    public void sendFollowRequest(Integer fromId, Integer toId){
        if(fromId == null) throw new NullPointerException("From id is null.");
        else if(toId == null) throw new NullPointerException("To id is null");
        User toUser = userDao.getUserByIdOrNickname(toId, null);
        if(toUser.getPrivacy() == "private"){
            sender.sendFollowRequest();
        }else{
            addDirectedFollow(fromId, toId);
            sender.sendFollowNotification();
        }
    }

    @Override
    public void unfollow(Integer fromId, Integer toId){
        if(fromId == null) throw new NullPointerException("From id is null.");
        else if(toId == null) throw new NullPointerException("To id is null");
        List <String> where = new ArrayList<>();
        where.add("from_used_id");
        where.add("to_user_id");
        try {
            String query = queries.getDeleteQuery(where);
            PreparedStatement stm = connection.prepareStatement(query);
            int changedRows = stm.executeUpdate();
            if(changedRows != 1){
                // add errors
            }
        } catch (InvalidSQLQueryException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DirectedFollow> getAllFollowers(Integer id){
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

    @Override
    public List<DirectedFollow> getAllFollowing(Integer id){
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

    @Override
    public void sendFollowResponse(Integer fromId, Integer toId) {
        if(fromId == null) throw new NullPointerException("From id is null.");
        else if(toId == null) throw new NullPointerException("To id is null");
        addDirectedFollow(toId, fromId);
        sender.sendFollowNotification();
    }

    private void addDirectedFollow(Integer fromId, Integer toId) throws DirectionalFollowNotAdded {
        if(fromId == null) throw new NullPointerException("From id is null.");
        else if(toId == null) throw new NullPointerException("To id is null");
        List <String> insertFields = new ArrayList<>();
        List <String> values = new ArrayList<>();
        insertFields.add("from_user_id");
        insertFields.add("to_user_id");
        try {
            String query = queries.getInsertQuery(insertFields);
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, fromId);
            stm.setInt(2, toId);
            int addedRows =  stm.executeUpdate();
            stm.close();
            if(addedRows == 0) throw new DirectionalFollowNotAdded("Directional follow is not added.");
        } catch (InvalidSQLQueryException | SQLException e) {
           //implement
        }

    }


}
