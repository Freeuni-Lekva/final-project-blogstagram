package org.blogstagram.errors;

public class DatabaseError extends Throwable {
    private String message;
    public DatabaseError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
