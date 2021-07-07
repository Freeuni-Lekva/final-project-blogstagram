package org.blogstagram.errors;

public class NotValidUserIdException extends Throwable{
    private String msg;
    public NotValidUserIdException(String message) {
        msg = message;
    }

    @Override
    public String toString() {
        return msg;
    }
}
