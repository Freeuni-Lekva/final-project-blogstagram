package org.blogstagram.sql;

import org.blogstagram.dao.BlogModeratorDao;
import org.blogstagram.dao.SqlBlogModeratorDao;

public class BlogModeratorQueries extends SqlQueries {
    private static final String REALTABLE = "blog_moderators";
    private static final String TESTTABLE = "";

    public BlogModeratorQueries(int usePurpose){
        if(usePurpose == SqlBlogModeratorDao.REAL) {
            this.tableName = "blog_moderators";
        }
    }
}
