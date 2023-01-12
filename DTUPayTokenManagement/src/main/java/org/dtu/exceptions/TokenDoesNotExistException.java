package org.dtu.exceptions;

public class TokenDoesNotExistException  extends Throwable{
        public TokenDoesNotExistException() {}

        public TokenDoesNotExistException(String msg) {
            super(msg);
        }
}
