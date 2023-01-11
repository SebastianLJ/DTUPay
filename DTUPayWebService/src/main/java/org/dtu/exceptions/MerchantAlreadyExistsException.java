package org.dtu.exceptions;

public class MerchantAlreadyExistsException extends Throwable {
    public MerchantAlreadyExistsException() {

    }
    public MerchantAlreadyExistsException(String msg) {
        super(msg);
    }
}

