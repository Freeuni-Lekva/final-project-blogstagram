package org.blogstagram.dao;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.HashTag;

import java.sql.SQLException;
import java.util.List;

public interface HashTagDao {
    void addHashTags(int blogId, List <HashTag> hashTags) throws InvalidSQLQueryException, SQLException, DatabaseError;
    void removeHashTags(int blogId, List <HashTag> hashTags) throws InvalidSQLQueryException, DatabaseError;
    List<HashTag> getHashTags(int blogId) throws InvalidSQLQueryException, DatabaseError;
    void editHashTags(Blog blog, List <HashTag> newHashTagList) throws DatabaseError, InvalidSQLQueryException;
}
