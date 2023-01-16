package org.dtu.exceptions;

public class PaymentDoesNotExist extends Throwable {
    //setup exception
    //setup empty constructor
    public PaymentDoesNotExist() {
    }
    public PaymentDoesNotExist(String message) {
        super(message);
    }

}
