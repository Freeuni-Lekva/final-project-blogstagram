package org.blogstagram.errors;

public class NotValidUserIdException extends Throwable{
    private final String msg;
    public NotValidUserIdException(String message) {
        msg = message;
    }

    @Override
    public String toString() {
        return msg;
    }
<<<<<<< HEAD
}
=======
}

>>>>>>> master
