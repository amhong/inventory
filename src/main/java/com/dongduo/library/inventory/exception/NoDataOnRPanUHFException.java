package com.dongduo.library.inventory.exception;

public class NoDataOnRPanUHFException extends RuntimeException {
    public NoDataOnRPanUHFException() {
    }

    public NoDataOnRPanUHFException(String message) {
        super(message);
    }

    public NoDataOnRPanUHFException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDataOnRPanUHFException(Throwable cause) {
        super(cause);
    }

    public NoDataOnRPanUHFException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
