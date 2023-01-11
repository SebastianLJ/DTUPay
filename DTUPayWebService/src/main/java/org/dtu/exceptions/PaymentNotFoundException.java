package org.dtu.exceptions;

public class PaymentNotFoundException extends Exception {
    public PaymentNotFoundException() {

    }
    public PaymentNotFoundException(String msg) {
        super(msg);
    }
}
