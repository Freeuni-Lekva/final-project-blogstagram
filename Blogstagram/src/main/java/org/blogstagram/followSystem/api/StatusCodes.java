package org.blogstagram.followSystem.api;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  StatusCodes {
    public static final int followed = 1;
    public static final int unfollowed = 2;
    public static final int requestSent = 3;
    public static final int gotAllFollowers = 4;
    public static final int gotAllFollowings = 5;
    public static final int requestApproved = 6;
    public static final int requestDeclined = 7;
    public static final int error = -1;
    // -----
    private static final String followMsg = "Follow request Success";
    private static final String unfollowMsg = "Unfollow request Success";
    private static final String requestSentMsg = "Sent";
    private static final String gotAllFollowersMsg = "got All Followers";
    private static final String gotAllFollowingsMsg = "got All Followings";
    private static final String requestApprovedMsg = "Approved";
    private static final String requestDeclinedMsg = "Declined";
    private static final String errorMsg = "Error";

    private static final Map <Integer, String>  mappings = new HashMap<>();


    public static Map<Integer, String> getMappings(){
        if(mappings.size() == 0){
            initialize();
        }
        return mappings;
    }

    private static void initialize() {
        mappings.put(followed, followMsg);
        mappings.put(unfollowed, unfollowMsg);
        mappings.put(requestSent, requestSentMsg);
        mappings.put(requestApproved, requestApprovedMsg);
        mappings.put(requestDeclined, requestDeclinedMsg);
        mappings.put(gotAllFollowers, gotAllFollowersMsg);
        mappings.put(gotAllFollowings, gotAllFollowingsMsg);
        mappings.put(error, errorMsg);
    }
}
