package com.yzqc.support.exception;

/**
 * Created by Administrator on 2015/8/10.
 */
public class ConfilctException extends RuntimeException {

    public ConfilctException() {
    }

    public ConfilctException(String message) {
        super(message);
    }

    public ConfilctException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfilctException(Throwable cause) {
        super(cause);
    }

    public ConfilctException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
