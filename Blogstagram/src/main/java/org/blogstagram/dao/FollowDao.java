package org.blogstagram.dao;

import org.blogstagram.models.DirectedFollow;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface FollowDao {
    boolean isAlreadyFollowed(Integer fromId, Integer toId);
    void sendFollowRequest(Integer fromId, Integer toId);
    void unfollow(Integer fromId, Integer toId);
    List<DirectedFollow> getAllFollowers(Integer id);
    List<DirectedFollow> getAllFollowing(Integer id);
    void sendFollowResponse(Integer fromId, Integer toId);
}
