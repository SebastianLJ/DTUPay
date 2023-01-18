package org.dtu.exceptions;

public class NoMoreValidTokensException extends Throwable{

    public NoMoreValidTokensException() {

    }

    public NoMoreValidTokensException(String msg) {
        super(msg);
    }
}