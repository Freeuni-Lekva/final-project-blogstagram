package org.blogstagram.sql;

import org.blogstagram.errors.InvalidSQLQueryException;

import java.util.List;

public class FollowQueries implements SqlQueries{
    @Override
    public String getUpdateQuery(List<String> updateFields, List<String> whereClause) {
        return null;
    }

    private void addWhereClause(StringBuilder builder, List <String> whereClause){
        builder.append("WHERE");
        for(int i = 0; i < whereClause.size() - 1; i++){
            builder.append(" ").append(whereClause.get(i)).append(" = ? and");
        }
        builder.append(" ").append(whereClause.get(whereClause.size() - 1)).append(" = ?");
    }

    @Override
    public String getSelectQuery(List<String> selectFields, List<String> whereClause) throws InvalidSQLQueryException {
        if(selectFields.size() == 0)
            throw new InvalidSQLQueryException("Query can't be generated.");
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT");
        for(int i = 0; i < selectFields.size() - 1; i++){
            builder.append(" ").append(selectFields.get(i)).append(",");
        }
        builder.append(" ");
        builder.append(selectFields.get(selectFields.size() - 1));
        builder.append(System.lineSeparator());
        builder.append("FROM follows");
        builder.append(System.lineSeparator());
        if(whereClause.size() != 0)
            addWhereClause(builder, whereClause);
        builder.append(";");
        return builder.toString();
    }

    @Override
    public String getDeleteQuery(List<String> whereClause) throws InvalidSQLQueryException {
        if(whereClause.size() == 0)
            throw new InvalidSQLQueryException("Query can't be generated.");
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE from follows ");
        addWhereClause(builder, whereClause);
        builder.append(";");
        return builder.toString();
    }

    @Override
    public String getInsertQuery(List<String> insertFields) throws InvalidSQLQueryException {
        if(insertFields.size() == 0)
            throw new InvalidSQLQueryException("Query can't be generated.");
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO follows (");
        for(int k = 0; k < insertFields.size() - 1; k++){
            builder.append(insertFields.get(k)).append(", ");
        }
        builder.append(insertFields.get(insertFields.size() - 1)).append(")");
        builder.append("VALUES");
        builder.append("(");
        for(int k = 0; k < insertFields.size() - 1; k++){
            builder.append("?").append(", ");
        }
        builder.append("?").append(");");
        return builder.toString();
    }
}
