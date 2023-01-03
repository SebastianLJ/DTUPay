package org.dtu;

public class PaymentAlreadyExistsException extends Exception {
    public PaymentAlreadyExistsException() {

    }
    public PaymentAlreadyExistsException(String msg) {
        super(msg);
    }
}
