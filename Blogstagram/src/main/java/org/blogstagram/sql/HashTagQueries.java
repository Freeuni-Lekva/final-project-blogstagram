package org.blogstagram.sql;


import org.blogstagram.dao.SqlHashTagDao;

public class HashTagQueries extends SqlQueries {
    private String tableName;
    private static String REALTABLE = "hashtags";
    public HashTagQueries(int usePurpose){
        if(usePurpose == SqlHashTagDao.REAL){
            this.tableName = REALTABLE;
        }
    }
}
