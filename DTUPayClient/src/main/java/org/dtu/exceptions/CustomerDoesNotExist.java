package org.dtu.exceptions;

public class CustomerDoesNotExist extends Throwable {
    //setup exception
    //setup empty constructor
    public CustomerDoesNotExist() {
    }
    public CustomerDoesNotExist(String message) {
        super(message);
    }

}
