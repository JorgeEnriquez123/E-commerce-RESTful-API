package com.jorge.ecommerce.handler.exception;

public class FailedLoginException extends RuntimeException{
    public FailedLoginException() {
    }

    public FailedLoginException(String message) {
        super(message);
    }

    public FailedLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedLoginException(Throwable cause) {
        super(cause);
    }

    public FailedLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
