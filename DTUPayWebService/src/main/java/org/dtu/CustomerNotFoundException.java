package org.dtu;

public class CustomerNotFoundException extends Throwable {
    public CustomerNotFoundException() {
        super();
    }
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
