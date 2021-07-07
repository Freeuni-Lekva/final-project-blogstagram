package org.blogstagram.dao;

import org.blogstagram.errors.DirectionalFollowNotAdded;
import org.blogstagram.models.DirectedFollow;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface FollowDao {
    boolean doesConnectionExist(DirectedFollow dFollow);
    void addDirectedFollow(DirectedFollow dFollow) throws DirectionalFollowNotAdded;
    void deleteFollow(DirectedFollow dFollow);
    public List<DirectedFollow> selectAllFollowers(Integer id);
    public List<DirectedFollow> selectAllFollowings(Integer id);
}
