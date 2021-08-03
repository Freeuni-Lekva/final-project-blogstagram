package org.blogstagram.tests.followSystemTests;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.sql.FollowQueries;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestFollowQueries {

    FollowQueries queries;

    @Before
    public void init(){
        queries = new FollowQueries(SqlFollowDao.TEST);
    }


    @Test
    public void testFollowQueries2(){
        assertNotNull(queries);
        List<String> selectClause = Arrays.asList("id", "from_user_id", "to_user_id");
        List <String> whereClause = Arrays.asList("id", "created_at");
        try {
            String query = queries.getSelectQuery(selectClause, whereClause);
            assertEquals("select id, from_user_id, to_user_id\r\nfrom follows_test_t \r\nwhere id = ? and created_at = ?;", query.toLowerCase(Locale.ROOT));
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testFollowQueries3(){
        assertNotNull(queries);
        List <String> whereClause = Arrays.asList("id", "from_user_id", "to_user_id", "created_at");
        try {
            String query = queries.getDeleteQuery(whereClause);
            assertEquals("delete from follows_test_t where id = ? and from_user_id = ? and to_user_id = ? and created_at = ?;", query.toLowerCase(Locale.ROOT));
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testFollowQueries4(){
        assertNotNull(queries);
        List <String> updateClause = new ArrayList<>();
        List <String> whereCluase = new ArrayList<>();
        String query = null;
        try {
            query = queries.getUpdateQuery(updateClause, whereCluase);
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
        assertNull(query);
    }

    @Test
    public void testFollowQueries5(){
        assertNotNull(queries);
        List <String> insertClause = Arrays.asList("id", "to_user_id", "from_user_id", "created_at");
        try {
            String query = queries.getInsertQuery(insertClause, 1);
            assertEquals("insert into follows_test_t (id, to_user_id, from_user_id, created_at)values(?, ?, ?, ?);", query.toLowerCase(Locale.ROOT));
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFollowQueries6(){
        assertNotNull(queries);
        final List <String> selectQueries = Collections.emptyList();
        final List <String> whereClause = Collections.singletonList("id");
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                queries.getSelectQuery(selectQueries, whereClause);
            }
        });
    }


    @Test
    public void testFollowQueries7() {
        assertNotNull(queries);

        final List <String> insertFields = Collections.emptyList();
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                queries.getInsertQuery(insertFields, 1);
            }
        });
    }

    @Test
    public void testFollowQueries8(){
        assertNotNull(queries);
        final List <String> whereClause = Collections.emptyList();
        assertThrows(InvalidSQLQueryException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                queries.getDeleteQuery(whereClause);
            }
        });
    }

    @Test
    public void testFollowQueries9(){
        List <String> whereClause = Collections.emptyList();
        List <String> selectClause = Arrays.asList("id", "from_user_id", "to_user_id");
        try {
            String query = queries.getSelectQuery(selectClause, whereClause);
            assertEquals("select id, from_user_id, to_user_id\r\nfrom follows_test_t \r\n;", query.toLowerCase(Locale.ROOT));
        } catch (InvalidSQLQueryException e) {
            e.printStackTrace();
        }
    }

}
