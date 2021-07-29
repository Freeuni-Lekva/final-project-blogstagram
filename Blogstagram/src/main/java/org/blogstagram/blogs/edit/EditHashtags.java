package org.blogstagram.blogs.edit;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;

public class EditHashtags implements Edit{

    private EditBlog editor;

    public EditHashtags(EditBlog editor) {
        if(editor == null) throw new NullPointerException("Edit Hash tag object can't be null.");
        this.editor = editor;
    }


    @Override
    public boolean mustEdit(Blog changeBlog, Blog currentBlog) {
        return changeBlog.getHashTagList().equals(currentBlog.getHashTagList());
    }

    @Override
    public void edit(Blog changeBlog, Blog currentBlog) throws DatabaseError, InvalidSQLQueryException {
        editor.editHashTags(currentBlog, changeBlog.getHashTagList());
    }
}
