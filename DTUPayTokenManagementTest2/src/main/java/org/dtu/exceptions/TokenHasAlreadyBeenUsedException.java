package org.dtu.exceptions;

public class TokenHasAlreadyBeenUsedException  extends Throwable{
    public TokenHasAlreadyBeenUsedException() {}

    public TokenHasAlreadyBeenUsedException(String msg) {
        super(msg);
    }
}
