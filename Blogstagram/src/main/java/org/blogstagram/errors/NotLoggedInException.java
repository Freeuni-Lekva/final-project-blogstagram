package org.blogstagram.errors;

public class NotLoggedInException extends Throwable{
    private String message;
    public NotLoggedInException(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
