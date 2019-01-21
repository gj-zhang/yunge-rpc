package com.rancho.yunge.exception;

import java.io.IOException;

/**
 * @author zgj-18063794
 * @createTime 2019-01-21 17:42
 */
public class BindPortException extends IOException {

    public BindPortException(String message) {
        super(message);
    }

    public BindPortException(String message, Throwable cause) {
        super(message, cause);
    }
}
