package org.blogstagram.tests.blogTests;

import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.sql.BlogQueries;
import org.blogstagram.sql.SqlQueries;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class testBlogQueries {
    private SqlQueries queries;

    @Before
    public void init(){
        queries = new BlogQueries(SqlBlogDAO.TEST);
    }

    @Test
    public void testInsertQuery(){
        List <String> insert = Arrays.asList("user_id", "title", "content");
        try {
            String query = queries.getInsertQuery(insert, 1);
            assertEquals(query.toLowerCase(Locale.ROOT), "insert into blogs_test_t (user_id, title, content)values(?, ?, ?);");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteQuery(){
        List <String> where = Arrays.asList("id");
        try {
            String query = queries.getDeleteQuery(where);
            assertEquals(query.toLowerCase(Locale.ROOT), "delete from blogs_test_t where id = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testUpdateQuery(){
        List <String> insert = Arrays.asList("title", "content");
        List <String> where = Arrays.asList("id", "user_id");
        try {
            String query = queries.getUpdateQuery(insert, where);
            assertEquals(query.toLowerCase(Locale.ROOT), "update blogs_test_t\r\nset title = ?, content = ?where id = ? and user_id = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectQuery(){
        List <String> select = Arrays.asList("id", "user_id", "title");
        List <String> where = Arrays.asList("id", "title");
        try {
            String query = queries.getSelectQuery(select, where);
            assertEquals(query.toLowerCase(Locale.ROOT), "select id, user_id, title\r\nfrom blogs_test_t \r\nwhere id = ? and title = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNoUpdateFields(){
        final List <String> updateFields = new ArrayList<>();
        final List <String> whereClause = new ArrayList<>();
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                String query = queries.getUpdateQuery(updateFields, whereClause);
            }
        });
    }

    @Test
    public void testUpdateWithNoWhereClause(){
        List <String> update = Arrays.asList("user_id", "content");
        List <String> where = new ArrayList<>();
        try {
            String query = queries.getUpdateQuery(update, where);
            assertEquals(query.toLowerCase(Locale.ROOT), "update blogs_test_t\r\nset user_id = ?, content = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectQueriesWithNoWhereClause(){
        List <String> select = Arrays.asList("id", "blog_id", "content");
        List <String> where = new ArrayList<>();
        try {
            String query = queries.getSelectQuery(select, where);
            assertEquals(query.toLowerCase(Locale.ROOT), "select id, blog_id, content\r\nfrom blogs_test_t \r\n;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testNoSelectQueries(){
        final List <String> select = new ArrayList<>();
        final List <String> where = Arrays.asList("id", "blog_id");
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    String query = queries.getSelectQuery(select, where);
                }
        });
    }

    @Test
    public void testNoDeleteQuery(){
        final List <String> delete = new ArrayList<>();
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                String query = queries.getDeleteQuery(delete);
            }
        });
    }

    @Test
    public void testNoInsertFields(){
        final List <String> insert = new ArrayList<>();
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                String query = queries.getInsertQuery(insert, 1);
            }
        });
    }

    @Test
    public void testInsertQuery2(){
        List <String> insert = Arrays.asList("user_id", "title", "content");
        try {
            String query = queries.getInsertQuery(insert, 2);
            assertEquals(query.toLowerCase(Locale.ROOT), "insert into blogs_test_t (user_id, title, content)values(?, ?, ?),\r\n(?, ?, ?);");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }

    }

}
