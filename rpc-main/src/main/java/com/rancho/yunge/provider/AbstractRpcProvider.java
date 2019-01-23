package com.rancho.yunge.provider;

import com.rancho.yunge.context.ContextHolder;

public abstract class AbstractRpcProvider implements RpcProvider {

    ContextHolder contextHolder;

    public AbstractRpcProvider(ContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }


}