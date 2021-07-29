package org.blogstagram.sql;

import org.blogstagram.dao.BlogModeratorDao;
import org.blogstagram.dao.SqlBlogModeratorDao;

public class BlogModeratorQueries extends SqlQueries {
    private static String REALTABLE = "blog_moderators";
    private static String TESTTABLE = "";

    public BlogModeratorQueries(int usePurpose){
        if(usePurpose == SqlBlogModeratorDao.REAL) {
            this.tableName = "blog_moderators";
        }
    }
}
