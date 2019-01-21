package com.rancho.yunge.exception;

/**
 * @author zgj-18063794
 * @createTime 2019-01-21 16:51
 */
public class YunGeRpcException extends Exception {

    public YunGeRpcException(String message) {
        super(message);
    }

    public YunGeRpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
