package org.blogstagram.sql;

import org.blogstagram.errors.InvalidSQLQueryException;

import java.util.List;

public interface SqlQueries {
    String getUpdateQuery(List<String> updateFields, List <String> whereClause);
    String getSelectQuery(List<String> selectFields, List<String> whereClause) throws InvalidSQLQueryException;
    String getDeleteQuery(List<String> whereClause) throws InvalidSQLQueryException;
    String getInsertQuery(List<String> insertFields) throws InvalidSQLQueryException;
}
