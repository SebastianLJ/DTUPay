package org.dtu.exceptions;

public class InvalidCustomerNameException extends Exception {
    public InvalidCustomerNameException() {

    }

    public InvalidCustomerNameException(String msg) {
        super(msg);
    }
}
