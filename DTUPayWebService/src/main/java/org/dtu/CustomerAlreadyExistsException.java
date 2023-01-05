package org.dtu;

public class CustomerAlreadyExistsException extends Throwable {
    public CustomerAlreadyExistsException() {

    }
    public CustomerAlreadyExistsException(String msg) {
        super(msg);
    }
}
