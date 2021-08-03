package org.blogstagram.blogs.edit;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;

import java.util.*;

public class EditModerators implements Edit {

    private final EditBlog blogEditor;

    public EditModerators(EditBlog blogEditor){
        this.blogEditor = blogEditor;
    }

    @Override
    public boolean mustEdit(Blog change, Blog currentBlog) {
        List <User> newModerators = change.getBlogModerators();
        List <User> currentModerators = currentBlog.getBlogModerators();
        if(newModerators.size() != currentModerators.size()) return true;
        Comparator <User> comparator = new Comparator<User>() {
            @Override
            public int compare(User first, User second) {
                return first.getId() - second.getId();
            }
        };
        Collections.sort(newModerators, comparator);
        Collections.sort(currentModerators, comparator);
        int index = 0;
        for(User currentUser : newModerators) {
            if(currentUser.getId().equals(currentModerators.get(index).getId())) return false;
            index++;
        }
        return true;
    }

    @Override
    public void edit(Blog change, Blog currentBlog) throws DatabaseError, InvalidSQLQueryException {
        List <User> newModerators = change.getBlogModerators();
        blogEditor.editModerators(currentBlog, newModerators);
    }
}
