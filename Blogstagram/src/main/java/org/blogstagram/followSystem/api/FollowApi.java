package org.blogstagram.followSystem.api;

import org.blogstagram.dao.FollowDao;
import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.followSystem.Validators.UserIdValidator;
import org.blogstagram.listeners.followNotificationSender;
import org.blogstagram.models.DirectedFollow;

import java.sql.Connection;
import java.util.List;

public class FollowApi {

    private FollowDao followDao;
    private UserDao userDao;
    private followNotificationSender sender;

    private DirectedFollow initializeDirectedFollowObj(Integer fromId, Integer toId) {
        DirectedFollow dFollow = new DirectedFollow();
        dFollow.setId(DirectedFollow.NO_ID);
        dFollow.setFromId(fromId);
        dFollow.setToId(toId);
        dFollow.setCreatedAt(DirectedFollow.defaultTimeValue);
        return dFollow;
    }

    public boolean alreadyFollowed(Integer fromId, Integer toId) throws NullPointerException{
        DirectedFollow dFollow = initializeDirectedFollowObj(fromId, toId);
        return followDao.doesConnectionExist(dFollow);
    }



    /*
        Function sends follow request. if to id user's privacy is public, addDirectedFollow method is used to write new row in database and
        sends follow notification to user. else if privacy is private it only sends request to user and waits for user response.
     */

    public int sendFollowRequest(Integer fromId, Integer toId) throws DirectionalFollowNotAdded {
        User user = userDao.getUserByIdOrNickname(toId);
        DirectedFollow dFollow = initializeDirectedFollowObj(fromId, toId);
        if(user.getPrivacy() == "private"){
            sender.sendFollowRequest();
            return StatusCodes.requestSent;
        }else{
            followDao.addDirectedFollow(dFollow);
            sender.sendFollowNotification();
            return StatusCodes.followed;
        }
    }

    public int unfollow(Integer fromId, Integer toId) { // throw user not
        DirectedFollow dFollow = initializeDirectedFollowObj(fromId, toId);
        followDao.deleteFollow(dFollow);
        return StatusCodes.unfollowed;
    }

    /*
        Function accepts to user's request for follow and adds row to database.
     */
    public int acceptFollowRequest(Integer fromId, Integer toId) throws DirectionalFollowNotAdded {
        DirectedFollow directedFollow = initializeDirectedFollowObj(fromId, toId);
        followDao.addDirectedFollow(directedFollow);
        return StatusCodes.requestApproved;
    }


    //user
    List <User> getAllFollowers(Integer id) throws NotValidUserIdException {
        UserIdValidator validator = new UserIdValidator();
        validator.validate(id);
        return followDao.selectAllFollowers(id);
    }
    //user
    List <User> getAllFollowing(Integer id) throws NotValidUserIdException {
        UserIdValidator validator = new UserIdValidator();
        validator.validate(id);
        return followDao.selectAllFollowings(id);
    }




    /*
        Registers follow dao object if follow dao object is null throws NUllPointer exception
     */
    public void setFollowDao(FollowDao followDao) {
        if(followDao == null) throw new NullPointerException("Follow dao object can't be null.");
        this.followDao = followDao;
    }





    /*
        Takes instance of followNotificationSender class and uses it to send notifications to user when he/she is followed, or
        follow request must be sent.
     */
    public void registerFollowRequestSender(followNotificationSender sender){
        if(sender == null) throw new NullPointerException("Follow request Sender can't be null.");
        this.sender = sender;
    }




}
