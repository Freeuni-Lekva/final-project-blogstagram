package org.blogstagram.sql;

import org.blogstagram.errors.InvalidSQLQueryException;

import java.util.List;

public abstract class SqlQueries {

     String tableName;

     void addWhereClause(StringBuilder builder, List<String> whereClause){
        builder.append("WHERE");
        for(int i = 0; i < whereClause.size() - 1; i++){
            builder.append(" ").append(whereClause.get(i)).append(" = ? and");
        }
        builder.append(" ").append(whereClause.get(whereClause.size() - 1)).append(" = ?");
    }


    public String getUpdateQuery(List<String> updateFields, List <String> whereClause) throws InvalidSQLQueryException {
         if(updateFields.size() == 0 || updateFields == null)
             throw new InvalidSQLQueryException("Query can't be generated");
         StringBuilder builder = new StringBuilder();
         builder.append("UPDATE ").append(tableName).append(System.getProperty("line.separator")).
                 append("SET ");
         for(int k = 0; k < updateFields.size() - 1; k++){
             builder.append(updateFields.get(k)).append(" = ?, ");
         }
         builder.append(updateFields.get(updateFields.size() - 1)).append(" = ?;");
         if(whereClause.size() != 0)
             addWhereClause(builder, whereClause);
         return builder.toString();
    }

    public String getSelectQuery(List<String> selectFields, List<String> whereClause) throws InvalidSQLQueryException{
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
        builder.append("FROM ").append(tableName).append(" ");
        builder.append(System.lineSeparator());
        if(whereClause.size() != 0)
            addWhereClause(builder, whereClause);
        builder.append(";");
        return builder.toString();
    }
    public String getDeleteQuery(List<String> whereClause) throws InvalidSQLQueryException{
        if(whereClause.size() == 0)
            throw new InvalidSQLQueryException("Query can't be generated.");
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE from ").append(tableName).append(" ");
        addWhereClause(builder, whereClause);
        builder.append(";");
        return builder.toString();
    }

    public String getInsertQuery(List<String> insertFields, int numRows) throws InvalidSQLQueryException{
        if(insertFields.size() == 0)
            throw new InvalidSQLQueryException("Query can't be generated.");
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(tableName).append(" (");
        for(int k = 0; k < insertFields.size() - 1; k++){
            builder.append(insertFields.get(k)).append(", ");
        }
        builder.append(insertFields.get(insertFields.size() - 1)).append(")");
        for(int j = 0; j < numRows; j++) {
            builder.append("VALUES");
            builder.append("(");
            for (int k = 0; k < insertFields.size() - 1; k++) {
                builder.append("?").append(", ");
            }
            builder.append("?").append(")");
            if(j != numRows - 1) builder.append(",").append(System.getProperty("line.separator"));
            else builder.append(";");
        }
        return builder.toString();
    }
}
