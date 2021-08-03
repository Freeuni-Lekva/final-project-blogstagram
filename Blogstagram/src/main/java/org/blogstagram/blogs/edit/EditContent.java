package org.blogstagram.blogs.edit;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;

public class EditContent implements Edit {

    private final EditBlog blogEditor;
    public EditContent(EditBlog blogEditor){
        this.blogEditor = blogEditor;
    }

    @Override
    public boolean mustEdit(Blog change, Blog currentBlog) {
        return !change.getContent().equals(currentBlog.getContent());
    }

    @Override
    public void edit(Blog change, Blog currentBlog) throws DatabaseError, InvalidSQLQueryException {
        blogEditor.editContent(currentBlog.getId(), change.getContent());
    }
}
