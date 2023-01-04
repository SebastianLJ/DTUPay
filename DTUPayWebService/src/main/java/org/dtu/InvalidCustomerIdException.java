package org.dtu;

public class InvalidCustomerIdException extends Exception {
    public InvalidCustomerIdException() {

    }

    public InvalidCustomerIdException(String msg) {
        super(msg);
    }
}
