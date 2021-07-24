package org.blogstagram.sql;

import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.errors.InvalidSQLQueryException;

import java.util.List;

public class FollowQueries extends SqlQueries{

    private static final String REALTABLE = "follows";
    private static final String TESTTABLE = "follows_test_t";

    public FollowQueries(int purpose){
        if(purpose == SqlFollowDao.TEST){
            this.tableName = TESTTABLE;
        }else if(purpose == SqlFollowDao.REAL){
            this.tableName = REALTABLE;
        } else{
            throw new IllegalArgumentException("Use purpose must be test or real.");
        }
    }
}