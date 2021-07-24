package org.blogstagram.sql;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.errors.InvalidSQLQueryException;

import java.util.List;

public class BlogQueries extends SqlQueries {

    private static final String REALTABLE = "blogs";
    private static final String TESTTABLE = "blogs_test_t";
    public BlogQueries(int purpose){
        if(purpose == SqlBlogDAO.REAL){
            tableName = REALTABLE;
        } else if(purpose == SqlBlogDAO.TEST){
            tableName = TESTTABLE;
        } else {
            throw new IllegalArgumentException("Use purpose must be Test or real.");
        }
    }
}
