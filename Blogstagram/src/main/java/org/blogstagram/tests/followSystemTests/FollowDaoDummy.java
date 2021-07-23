package org.blogstagram.tests.followSystemTests;

import org.blogstagram.dao.FollowDao;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.models.DirectedFollow;
import org.blogstagram.models.User;

import java.util.*;

public class FollowDaoDummy implements FollowDao {

    private Map<Integer, Set<Integer>> followers;
    private Map <Integer, Set <Integer> > followings;

    public FollowDaoDummy(){
        followers = new HashMap<>();
        followings = new HashMap<>();
    }

    @Override
    public boolean doesConnectionExist(DirectedFollow dFollow) throws DatabaseError {
        if(!followings.containsKey(dFollow.getFromId())) return false;
        Set <Integer> followingsSet = followings.get(dFollow.getFromId());
        return followingsSet.contains(dFollow.getToId());
    }

    @Override
    public void addDirectedFollow(DirectedFollow dFollow) throws DirectionalFollowNotAdded, DatabaseError {
        if(!followings.containsKey(dFollow.getFromId())){
            Set <Integer> followingsSet = new HashSet<>();
            followingsSet.add(dFollow.getToId());
            followings.put(dFollow.getFromId(), followingsSet);
        } else{
            followings.get(dFollow.getFromId()).add(dFollow.getToId());
        }

        if(!followers.containsKey(dFollow.getToId())){
            Set <Integer> followersSet =  new HashSet<>();
            followersSet.add(dFollow.getFromId());
            followers.put(dFollow.getToId(), followersSet);
        }else{
            followers.get(dFollow.getToId()).add(dFollow.getFromId());
        }

    }

    @Override
    public void deleteFollow(DirectedFollow dFollow) throws DatabaseError {
        if(!followings.containsKey(dFollow.getFromId())) throw new DatabaseError("Database error");
        if(!followings.get(dFollow.getFromId()).contains(dFollow.getToId())) throw new DatabaseError("Database error");;
        followings.get(dFollow.getFromId()).remove(dFollow.getToId());
        if(followings.get(dFollow.getFromId()).size() == 0) followings.remove(dFollow.getFromId());
        followers.get(dFollow.getToId()).remove(dFollow.getFromId());
        if(followers.get(dFollow.getToId()).size() == 0) followers.remove(dFollow.getToId());
    }

    @Override
    public List<User> selectAllFollowers(Integer id) throws DatabaseError {
        if(!followers.containsKey(id)) return null;
        Set <Integer> followingsSet = followers.get(id);
        final List <User> result = new ArrayList<>();
        for(Integer currentId : followingsSet) {
            result.add(new User(currentId, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null));
        }
        return result;
    }

    @Override
    public List<User> selectAllFollowings(Integer id) throws DatabaseError {
        if(!followings.containsKey(id)) return null;
        Set <Integer> followersSet = followings.get(id);
        final List <User> result = new ArrayList<>();
        for(Integer currentId : followersSet){
            result.add(new User(currentId, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null));
        }
        return result;
    }
}
