package org.dtu.exceptions;

public class PaymentAlreadyExistsException extends Exception {
    public PaymentAlreadyExistsException() {

    }
    public PaymentAlreadyExistsException(String msg) {
        super(msg);
    }
}
