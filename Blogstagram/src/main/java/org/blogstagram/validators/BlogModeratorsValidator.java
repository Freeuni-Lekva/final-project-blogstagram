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

    public BlogModeratorsValidator(List <User> moderators){
        this.moderators = moderators;
        this.errors = new ArrayList<>();
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
               errors.add(new VariableError("User", "Blog moderator must be Real user."));
               return false;
           }
           else{
               if(used.contains(moderator.getId())) {
                   errors.add(new VariableError("User", "Blog moderators list must be unique."));
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
