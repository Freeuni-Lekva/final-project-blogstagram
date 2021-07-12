package org.blogstagram.followSystem.api;

import org.blogstagram.dao.FollowDao;
import org.blogstagram.dao.UserDAO;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.listeners.followNotificationSender;
import org.blogstagram.models.DirectedFollow;
import org.blogstagram.models.User;
import java.sql.SQLException;
import java.util.List;


public class FollowApi {

    private FollowDao followDao;
    private UserDAO userDao;

    public void setUserDao(UserDAO userDao) {
        if(userDao == null) throw new NullPointerException("userDao object can't be null.");
        this.userDao = userDao;
    }

    private followNotificationSender sender;

    private DirectedFollow initializeDirectedFollowObj(Integer fromId, Integer toId) {
        DirectedFollow dFollow = new DirectedFollow();
        dFollow.setId(DirectedFollow.NO_ID);
        dFollow.setFromId(fromId);
        dFollow.setToId(toId);
        dFollow.setCreatedAt(DirectedFollow.defaultTimeValue);
        return dFollow;
    }

    public boolean alreadyFollowed(Integer fromId, Integer toId) throws NullPointerException, DatabaseError {
        DirectedFollow dFollow = initializeDirectedFollowObj(fromId, toId);
        return followDao.doesConnectionExist(dFollow);
    }



    /*
        Function sends follow request. if to id user's privacy is public, addDirectedFollow method is used to write new row in database and
        sends follow notification to user. else if privacy is private it only sends request to user and waits for user response.
     */

    public void sendFollowRequest(Integer fromId, Integer toId) throws DirectionalFollowNotAdded, DatabaseError {
        try {
           User user = userDao.getUserByID(toId);
           DirectedFollow dFollow = initializeDirectedFollowObj(fromId, toId);
           if(user.getPrivacy().equals(User.PRIVATE)){ // add if private account -----------
               sender.sendFollowRequest();
           }else{
               followDao.addDirectedFollow(dFollow);
               sender.sendFollowNotification();
            }
        } catch (SQLException exception) {
            throw new DatabaseError("Can't find user id.");
        }

    }

    public void unfollow(Integer fromId, Integer toId) throws DatabaseError { // throw user not
        DirectedFollow dFollow = initializeDirectedFollowObj(fromId, toId);
        followDao.deleteFollow(dFollow);
    }

    /*
        Function accepts to user's request for follow and adds row to database.
     */
    public void acceptFollowRequest(Integer fromId, Integer toId) throws DirectionalFollowNotAdded, DatabaseError {
        DirectedFollow directedFollow = initializeDirectedFollowObj(fromId, toId);
        followDao.addDirectedFollow(directedFollow);
        sender.sendFollowNotification();
    }


    //user
    public List<User> getAllFollowers(Integer id) throws NotValidUserIdException, DatabaseError {
        return followDao.selectAllFollowers(id);
    }
    //user
    public List <User> getAllFollowing(Integer id) throws NotValidUserIdException, DatabaseError {
        return followDao.selectAllFollowings(id);
    }




    /*
        Registers follow dao object if follow dao object is null throws NUllPointer exception
     */
    public void setFollowDao(FollowDao followDao) {
        if(followDao == null) throw new NullPointerException("Follow dao object can't be null.");
        this.followDao = followDao;
    }

    // think for better design


    /*
        Takes instance of followNotificationSender class and uses it to send notifications to user when he/she is followed, or
        follow request must be sent.
     */
    public void registerFollowRequestSender(followNotificationSender sender){
        if(sender == null) throw new NullPointerException("Follow request Sender can't be null.");
        this.sender = sender;
    }




}
