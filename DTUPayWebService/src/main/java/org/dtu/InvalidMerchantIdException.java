package org.dtu;

public class InvalidMerchantIdException extends Exception {
    public InvalidMerchantIdException() {
    }
    public InvalidMerchantIdException(String msg) {
        super(msg);
    }
}
