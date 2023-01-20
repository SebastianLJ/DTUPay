package org.dtu.exceptions;
/**
 * @author Nicklas Olabi (s205347)
 */
public class MerchantNotFoundException extends Throwable {

    public MerchantNotFoundException() {
        super();
    }

    public MerchantNotFoundException(String msg) {
        super(msg);
    }
}
