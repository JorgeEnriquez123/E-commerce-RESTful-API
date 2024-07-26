package com.jorge.ecommerce.handlers.exception;

public class InsufficientProductStock extends RuntimeException {
    public InsufficientProductStock() {
    }

    public InsufficientProductStock(String message) {
        super(message);
    }

    public InsufficientProductStock(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientProductStock(Throwable cause) {
        super(cause);
    }

    public InsufficientProductStock(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
