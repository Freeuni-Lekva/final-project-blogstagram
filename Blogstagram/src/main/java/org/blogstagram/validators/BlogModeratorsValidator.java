package org.blogstagram.validators;

import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.VariableError;
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

    public BlogModeratorsValidator(List <User> moderators, int creatorId){
        this.moderators = moderators;
        this.errors = new ArrayList<>();
        this.creatorId = creatorId;
    }

    @Override
    public boolean validate() throws SQLException {
        if(moderators == null) {
            errors.add(new VariableError("", ""));
            return false;
        }

        Set <Integer> used = new TreeSet<>();

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
               }
               used.add(moderator.getId());
           }
        }

        return true;

    }

    @Override
    public List<GeneralError> getErrors() {
        return errors;
    }
}
