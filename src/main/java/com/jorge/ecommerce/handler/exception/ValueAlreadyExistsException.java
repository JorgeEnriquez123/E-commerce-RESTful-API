package com.jorge.ecommerce.handler.exception;

public class ValueAlreadyExistsException extends RuntimeException{
    public ValueAlreadyExistsException() {
    }

    public ValueAlreadyExistsException(String message) {
        super(message);
    }

    public ValueAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ValueAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
