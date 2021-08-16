package org.blogstagram.tests.blogTests;

import org.blogstagram.dao.SqlBlogModeratorDao;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.sql.BlogModeratorQueries;
import org.blogstagram.sql.SqlQueries;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class BlogModeratorQueriesTest {
    SqlQueries queries;

    @Before
    public void init(){
        queries = new BlogModeratorQueries(SqlBlogModeratorDao.TEST);
    }

    @Test
    public void testInsertQuery(){
        List<String> insertClause = Arrays.asList("blog_id", "user_id");
        try {
            String query = queries.getInsertQuery(insertClause, 1);
            assertEquals(query.toLowerCase(Locale.ROOT), "insert into blog_moderators_t (blog_id, user_id)values(?, ?);");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

    @Test

    public void testUpdateQuery(){
        List <String>  updateClause = Arrays.asList("blog_id", "user_id");
        List <String> whereClause = Arrays.asList("blog_id");
        try {
            String query = queries.getUpdateQuery(updateClause, whereClause);
            assertEquals(query.toLowerCase(Locale.ROOT), "update blog_moderators_t\r\nset blog_id = ?, user_id = ?where blog_id = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }

    }
  @Test
    public void testSelectQuery(){
        List <String> select = Arrays.asList("blog_id", "id");
        List <String> where = Arrays.asList("id");
        try {
            String query = queries.getSelectQuery(select, where);
            assertEquals(query.toLowerCase(Locale.ROOT), "select blog_id, id\r\nfrom blog_moderators_t \r\nwhere id = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDeleteQuery(){
        List <String> where = Arrays.asList("id", "blog_id");
        try {
            String query = queries.getDeleteQuery(where);
            assertEquals(query.toLowerCase(Locale.ROOT), "delete from blog_moderators_t where id = ? and blog_id = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testInsertQueryMultiple(){
        List <String> insert = Arrays.asList("user_id", "title", "content");
        try {
            String query = queries.getInsertQuery(insert, 3);
            assertEquals(query.toLowerCase(Locale.ROOT), "insert into blog_moderators_t (user_id, title, content)values(?, ?, ?),\r\n(?, ?, ?),\r\n(?, ?, ?);");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }


}
