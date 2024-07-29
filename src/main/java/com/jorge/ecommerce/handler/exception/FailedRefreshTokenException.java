package com.jorge.ecommerce.handler.exception;

public class FailedRefreshTokenException extends RuntimeException {
    public FailedRefreshTokenException() {
    }

    public FailedRefreshTokenException(String message) {
        super(message);
    }

    public FailedRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedRefreshTokenException(Throwable cause) {
        super(cause);
    }

    public FailedRefreshTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
