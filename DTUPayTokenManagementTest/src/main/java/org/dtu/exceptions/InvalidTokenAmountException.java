package org.dtu.exceptions;

public class InvalidTokenAmountException extends Throwable{

    public InvalidTokenAmountException() {

    }

    public InvalidTokenAmountException(String msg) {
        super(msg);
    }
}
