package org.dtu.exceptions;

public class CustomerAlreadyExistsException extends Throwable {
    public CustomerAlreadyExistsException() {

    }
    public CustomerAlreadyExistsException(String msg) {
        super(msg);
    }
}
