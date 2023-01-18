package org.dtu.exceptions;

public class InvalidTokenAmountRequestException extends Throwable{

    public InvalidTokenAmountRequestException() {

    }

    public InvalidTokenAmountRequestException(String msg) {
        super(msg);
    }
}
