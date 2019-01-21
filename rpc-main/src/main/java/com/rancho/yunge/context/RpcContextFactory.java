package com.rancho.yunge.context;

/**
 * @author zgj-18063794
 * @createTime 2019-01-21 16:16
 */
public class RpcContextFactory {

    public static ContextHolder getDefaultInstance() {
        return new DefaultContextHolder();
    }

}
