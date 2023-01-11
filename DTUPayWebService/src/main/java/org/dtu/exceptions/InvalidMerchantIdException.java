package org.dtu.exceptions;

public class InvalidMerchantIdException extends Exception {
    public InvalidMerchantIdException() {
    }
    public InvalidMerchantIdException(String msg) {
        super(msg);
    }
}
