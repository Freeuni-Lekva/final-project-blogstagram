package org.blogstagram.followSystemTests;
import org.blogstagram.dao.SqlFollowDao;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.sql.FollowQueries;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        String query = queries.getUpdateQuery(updateClause, whereCluase);
        assertNull(query);
    }

    @Test
    public void testFollowQueries5(){
        assertNotNull(queries);
        List <String> insertClause = Arrays.asList("id", "to_user_id", "from_user_id", "created_at");
        try {
            String query = queries.getInsertQuery(insertClause);
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
        assertThrows(InvalidSQLQueryException.class, () -> queries.getSelectQuery(selectQueries, whereClause));
    }


    @Test
    public void testFollowQueries7() {
        assertNotNull(queries);

        List <String> insertFields = Collections.emptyList();
        assertThrows(InvalidSQLQueryException.class, () -> queries.getInsertQuery(insertFields));
    }

    @Test
    public void testFollowQueries8(){
        assertNotNull(queries);
        List <String> whereClause = Collections.emptyList();
        assertThrows(InvalidSQLQueryException.class, () -> queries.getDeleteQuery(whereClause));
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
