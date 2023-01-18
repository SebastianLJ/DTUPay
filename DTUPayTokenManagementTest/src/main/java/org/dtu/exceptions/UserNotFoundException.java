package org.dtu.exceptions;

public class UserNotFoundException extends Throwable{

    public UserNotFoundException() {

    }

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
