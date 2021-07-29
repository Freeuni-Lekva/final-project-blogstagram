package org.blogstagram.dao;

import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.HashTag;
import org.blogstagram.sql.HashTagQueries;
import org.blogstagram.sql.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SqlHashTagDao implements HashTagDao {

    private final SqlQueries hashTagQueries;
    public static final int TEST = 0;
    public static final int REAL = 1;
    private Connection connection;
    public SqlHashTagDao(Connection connection) {
        if(connection == null) throw new NullPointerException("Connection object can;t be null.");
        hashTagQueries = new HashTagQueries(REAL);
        this.connection = connection;
    }

    @Override
    public void addHashTags(int blogId, List<HashTag> hashTags) throws InvalidSQLQueryException, DatabaseError {
        String query = hashTagQueries.getInsertQuery(Arrays.asList("blog_id", "hashtag"), hashTags.size());
        PreparedStatement prpStm = null;
        try {
            prpStm = connection.prepareStatement(query);

        int paramterIndex = 1;
        for(HashTag hashTag : hashTags){
            prpStm.setInt(paramterIndex++, hashTag.getBlogId());
            prpStm.setString(paramterIndex++, hashTag.getHashTag());
        }
        int affectedRows = prpStm.executeUpdate();
        assertEquals(affectedRows, hashTags.size());
        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to database");
        }
    }

    @Override
    public void removeHashTags(int blogId, List <HashTag> hashTags) throws InvalidSQLQueryException, DatabaseError {
        for(HashTag tag : hashTags){
            String query = hashTagQueries.getDeleteQuery(Arrays.asList("id"));
            try {
                PreparedStatement prpStm = connection.prepareStatement(query);
                prpStm.setInt(1, tag.getId());
                int affectedRows = prpStm.executeUpdate();
                assertEquals(1, affectedRows);
            } catch (SQLException exception) {
                throw new DatabaseError("Can't connect to database.");
            }
        }
    }

    @Override
    public List<HashTag> getHashTags(int blogId) throws InvalidSQLQueryException, DatabaseError {
        String query = hashTagQueries.getSelectQuery(Arrays.asList("id", "blog_id", "hashtag"), Collections.singletonList("blog_id"));
        List <HashTag> hashTags = new ArrayList<>();
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, blogId);
            ResultSet res = prpStm.executeQuery();
            while(!res.next()){
               HashTag tag = new HashTag(res.getString(3));
               tag.setId(res.getInt(1));
               tag.setBlogId(res.getInt(2));
               hashTags.add(tag);
            }

        } catch (SQLException exception) {
            throw new DatabaseError("Can't connect to database");
        }
        return hashTags;
    }

    @Override
    public void editHashTags(Blog blog, List<HashTag> newHashTagList) throws DatabaseError, InvalidSQLQueryException {
        List <HashTag> current = blog.getHashTagList();
        removeHashTags(blog.getId(), current);
        addHashTags(blog.getId(), newHashTagList);
    }
}
