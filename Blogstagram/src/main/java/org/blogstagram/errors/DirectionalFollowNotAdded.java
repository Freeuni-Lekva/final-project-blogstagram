package org.blogstagram.errors;

public class DirectionalFollowNotAdded extends Throwable{
    private String message;
    public DirectionalFollowNotAdded(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
