package org.blogstagram.dao;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.models.DirectedFollow;
import org.blogstagram.models.User;

import java.util.List;

public interface FollowDao {
    boolean doesConnectionExist(DirectedFollow dFollow) throws DatabaseError;
    void addDirectedFollow(DirectedFollow dFollow) throws DirectionalFollowNotAdded, DatabaseError;
    void deleteFollow(DirectedFollow dFollow) throws DatabaseError;
    List <User> selectAllFollowers(Integer id) throws DatabaseError;
    List <User> selectAllFollowings(Integer id) throws DatabaseError;
}
