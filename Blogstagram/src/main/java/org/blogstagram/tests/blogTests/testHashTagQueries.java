package org.blogstagram.tests.blogTests;

import org.blogstagram.dao.SqlHashTagDao;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.sql.HashTagQueries;
import org.blogstagram.sql.SqlQueries;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.util.*;

import static org.junit.Assert.*;

public class testHashTagQueries {
    private SqlQueries queries;

    @Before
    public void init(){
        queries = new HashTagQueries(SqlHashTagDao.TEST);
    }

    @Test
    public void testUpdateQuery(){
        List<String> updates = Arrays.asList("blog_id", "hashtag");
        List <String> where = Arrays.asList("blog_id");
        try {
            String query = queries.getUpdateQuery(updates, where);
            assertEquals(query.toLowerCase(Locale.ROOT), "update hashtags_t\r\nset blog_id = ?, hashtag = ?where blog_id = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNoUpdateFields(){
        final List <String> updates = new ArrayList<>();
        final List <String> where = new ArrayList<>();
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                String query = queries.getUpdateQuery(updates, where);
            }
        });
    }

    @Test
    public void testNoWhereClause(){
        List <String> update = Arrays.asList("blog_id", "hashtag");
        List <String> where = new ArrayList<>();
        try {
            String  query = queries.getUpdateQuery(update, where);
            assertEquals(query.toLowerCase(Locale.ROOT), "update hashtags_t\r\nset blog_id = ?, hashtag = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectQuery(){
        List <String> select = Arrays.asList("blog_id", "hashtag");
        List <String> where = Arrays.asList("id");
        try {
            String query = queries.getSelectQuery(select, where);
            assertEquals(query.toLowerCase(Locale.ROOT), "select blog_id, hashtag\r\nfrom hashtags_t \r\nwhere id = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testNoSelectFields(){
        final List <String> select = Collections.emptyList();
        final List <String> where = Arrays.asList("id");
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                String query = queries.getSelectQuery(select, where);
            }
        });

    }

    @Test
    public void testSelectNoWhereClause(){
        List <String> select = Arrays.asList("id");
        List <String> where = Collections.emptyList();
        try {
            String query = queries.getSelectQuery(select, where);
            assertEquals(query.toLowerCase(Locale.ROOT), "select id\r\nfrom hashtags_t \r\n;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDeleteQuery(){
        List <String> where = Arrays.asList("id");
        try {
            String query = queries.getDeleteQuery(where);
            assertEquals(query.toLowerCase(Locale.ROOT), "delete from hashtags_t where id = ?;");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNoWhereClauseDeleteQuery(){
        final List <String> where = Collections.emptyList();
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                String query = queries.getDeleteQuery(where);
            }
        });
    }


    @Test
    public void testInsertQuery(){
        List <String> insert = Arrays.asList("blog_id", "hashtag");
        try {
            String query = queries.getInsertQuery(insert, 1);
            assertEquals(query.toLowerCase(Locale.ROOT), "insert into hashtags_t (blog_id, hashtag)values(?, ?);");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testNoInsertFields(){
        final List <String> insert = Collections.emptyList();
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                String query = queries.getInsertQuery(insert, 2);
            }
        });
    }

    @Test
    public void testInsertMultipleRows(){
        List <String> insert = Arrays.asList("blog_id", "hashtag");
        try {
            String  query = queries.getInsertQuery(insert, 2);
            assertEquals(query.toLowerCase(Locale.ROOT), "insert into hashtags_t (blog_id, hashtag)values(?, ?),\r\n(?, ?);");
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

}
