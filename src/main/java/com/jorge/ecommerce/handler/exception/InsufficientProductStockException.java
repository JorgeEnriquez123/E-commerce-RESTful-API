package com.jorge.ecommerce.handler.exception;

public class InsufficientProductStockException extends RuntimeException {
    public InsufficientProductStockException() {
    }

    public InsufficientProductStockException(String message) {
        super(message);
    }

    public InsufficientProductStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientProductStockException(Throwable cause) {
        super(cause);
    }

    public InsufficientProductStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
