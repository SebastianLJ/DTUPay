package org.dtu.exceptions;

public class UserAlreadyExistsException extends Throwable{

    public UserAlreadyExistsException() {

    }

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
