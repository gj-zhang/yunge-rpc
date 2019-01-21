package com.rancho.yunge.util;

/**
 * @author zgj-18063794
 * @createTime 2019-01-21 16:57
 */
public class PreCondition {

    public static void check(boolean expression, String msg) {
        if (expression) {
            throw new IllegalArgumentException(msg);
        }
    }

}
