package org.blogstagram.blogs.edit;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;

public class EditTitle implements Edit {

    private final EditBlog blogEditor;

    public EditTitle(EditBlog blogEditor){
        this.blogEditor = blogEditor;
    }


    @Override
    public boolean mustEdit(Blog elem, Blog currentBlog) {
        return !currentBlog.getTitle().equals(elem.getTitle());
    }

    @Override
    public void edit(Blog elem, Blog currentBlog) throws DatabaseError, InvalidSQLQueryException {
        blogEditor.editTitle(currentBlog.getId(), elem.getTitle());
    }
}
