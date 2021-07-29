package org.blogstagram.dao;

import com.sun.org.apache.regexp.internal.RE;
import org.blogstagram.errors.DatabaseError;
import org.blogstagram.errors.GeneralError;
import org.blogstagram.errors.InvalidSQLQueryException;
import org.blogstagram.models.Blog;
import org.blogstagram.models.User;
import org.blogstagram.sql.BlogModeratorQueries;
import org.blogstagram.sql.SqlQueries;
import org.blogstagram.validators.BlogIdValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class SqlBlogModeratorDao implements BlogModeratorDao{

    private final SqlQueries moderatorQueries;
    private final Connection connection;
    private UserDAO userDao;
    public static final int TEST = 0;
    public static final int REAL = 1;

    public SqlBlogModeratorDao(Connection connection) {
        if(connection == null) throw new IllegalArgumentException("Connection is null.");
        this.connection = connection;
        moderatorQueries = new BlogModeratorQueries(REAL);
    }

    public void setUserDao(UserDAO userDao){
        if(userDao == null)
            throw new NullPointerException("User dao object can't be null.");
        this.userDao = userDao;
    }

    @Override
    public List<User> getModerators(int blogId) throws InvalidSQLQueryException, DatabaseError {
        try {
            String query = moderatorQueries.getSelectQuery(Collections.singletonList("user_id"), Collections.singletonList("blog_id"));
            PreparedStatement prpStm = connection.prepareStatement(query);
            prpStm.setInt(1, blogId);
            List <User> result = new ArrayList<>();
            ResultSet resultSet = prpStm.executeQuery();
            while (resultSet.next()){
                result.add(userDao.getUserByID(resultSet.getInt(1)));
            }
            return result;
        } catch (SQLException exception) {
            throw new DatabaseError("Query can't be Generated");
        }
    }

    public void addModerators(int blogId, List <User> moderators) throws InvalidSQLQueryException, DatabaseError {
        String query = moderatorQueries.getInsertQuery(Arrays.asList("blog_id", "user_id"), moderators.size());
        int paramIndex = 1;
        try {
            PreparedStatement prpStm = connection.prepareStatement(query);
            //System.out.println(moderators.size());
            int id = 1;
            for (User moderator : moderators) {
                prpStm.setInt(paramIndex++, id++);
                prpStm.setInt(paramIndex++, blogId);
                prpStm.setInt(paramIndex++, moderator.getId());
            }
            int affectedRows = prpStm.executeUpdate();
            assertEquals(affectedRows, moderators.size());
        } catch (SQLException exception) {
            throw new DatabaseError("Can't Connect to database.");
        }

    }

    @Override
    public void removeModerators(int blogId, List<User> moderators) throws InvalidSQLQueryException {
        for (User moderator : moderators) {
            String query = moderatorQueries.getDeleteQuery(Collections.singletonList("user_id"));
            try {
                PreparedStatement prpStm = connection.prepareStatement(query);
                prpStm.setInt(1, moderator.getId());
                int affectedRows = prpStm.executeUpdate();
                assertEquals(1, affectedRows);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void editModerators(Blog blog, List<User> newModerators) throws InvalidSQLQueryException, DatabaseError {
        Set<User> add = new HashSet<>(newModerators);
        Set <User> remove = new HashSet<>(blog.getBlogModerators());
        Set <User> intersection = new HashSet<>(add);
        intersection.retainAll(remove);
        add.removeAll(intersection);
        remove.removeAll(intersection);
        removeModerators(blog.getId(), new ArrayList<>(remove));
        addModerators(blog.getId(), new ArrayList<>(add));
    }
}
