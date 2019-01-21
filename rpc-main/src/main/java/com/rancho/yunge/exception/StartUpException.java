package com.rancho.yunge.exception;

/**
 * @author zgj-18063794
 * @createTime 2019-01-21 16:51
 */
public class StartUpException extends RuntimeException {

    public StartUpException(String message) {
        super(message);
    }

    public StartUpException(String message, Throwable cause) {
        super(message, cause);
    }
}
