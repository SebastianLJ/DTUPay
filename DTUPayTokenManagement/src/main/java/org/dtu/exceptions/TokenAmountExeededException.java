package org.dtu.exceptions;

public class TokenAmountExeededException extends Throwable{

    public TokenAmountExeededException() {

    }

    public TokenAmountExeededException(String msg) {
        super(msg);
    }
}
