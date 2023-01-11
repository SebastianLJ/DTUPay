package org.dtu.exceptions;

public class CustomerNotFoundException extends Throwable {
    public CustomerNotFoundException() {
        super();
    }
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
