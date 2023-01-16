package org.dtu.exceptions;

public class PaymentAlreadyExists extends Throwable {
    //empty constructor
    public PaymentAlreadyExists() {
    }
    public PaymentAlreadyExists(String message) {
        super(message);
    }

}
