package org.blogstagram.errors;

public class UserNotRegisteredException extends Throwable {
    private String message;
    public UserNotRegisteredException(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
