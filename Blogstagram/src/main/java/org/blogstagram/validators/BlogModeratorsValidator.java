package org.blogstagram.validators;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.NotValidUserIdException;
import org.blogstagram.errors.VariableError;
import org.blogstagram.followSystem.api.FollowApi;
import org.blogstagram.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BlogModeratorsValidator implements Validator{
    private final List <User> moderators;
    private final List <GeneralError> errors;
    private final Integer creatorId;
    private final FollowApi api;

    public BlogModeratorsValidator(List <User> moderators, int creatorId, FollowApi api){
        this.moderators = moderators;
        this.errors = new ArrayList<>();
        this.creatorId = creatorId;
        this.api = api;
    }

    @Override
    public boolean validate() throws SQLException {
        if(moderators == null) {
            errors.add(new VariableError("", ""));
            return false;
        }

        Set <Integer> used = new TreeSet<>();
        try {
            List <User> followers = api.getAllFollowing(creatorId);


            for(User moderator : moderators){
               if(moderator == null) {
                   errors.add(new VariableError("moderator", "Blog moderator must be Real user."));
                   return false;
               }
               else{
                   if(used.contains(moderator.getId())) {
                       errors.add(new VariableError("moderator", "Blog moderators list must be unique."));
                       return false;
                   }
                   else if(moderator.getId().equals(creatorId)){
                       errors.add(new VariableError("moderator", "Blog creator can't be moderator"));
                       return false;
                   } else if(!containsId(moderator.getId(), followers)){
                       errors.add(new VariableError("moderator", "Blog moderator must be follower"));
                       return false;
                   }
                   used.add(moderator.getId());
               }
            }
        } catch (NotValidUserIdException | DatabaseError e) {
            e.printStackTrace();
        }

        return true;

    }

    private boolean containsId(Integer id, List<User> followers) {
        for(User current : followers) {
            System.out.println(current.getId());
            System.out.println(id);
            if(current.getId().equals(id)) return true;
        }
        return false;
    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
