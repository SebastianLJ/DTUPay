package org.dtu.exceptions;

public class InvalidCustomerIdException extends Exception {
    public InvalidCustomerIdException() {

    }

    public InvalidCustomerIdException(String msg) {
        super(msg);
    }
}
