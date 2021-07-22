package org.blogstagram.errors;

public class InvalidSQLQueryException extends Throwable {
    private final String message;
    public InvalidSQLQueryException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
