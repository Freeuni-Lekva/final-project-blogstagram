package org.blogstagram.sql;


import org.blogstagram.dao.SqlHashTagDao;

public class HashTagQueries extends SqlQueries {
    private static final String REALTABLE = "hashtags";
    private static final String TESTTABLE = "hashtags_t";
    public HashTagQueries(int usePurpose){
        if(usePurpose == SqlHashTagDao.REAL){
            this.tableName = REALTABLE;
        } else if(usePurpose == SqlHashTagDao.TEST) {
            this.tableName = TESTTABLE;
        }
    }
}
