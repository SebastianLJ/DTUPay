package org.dtu.exceptions;

public class MerchantNotFoundException extends Throwable {

    public MerchantNotFoundException() {
        super();
    }

    public MerchantNotFoundException(String msg) {
        super(msg);
    }
}
