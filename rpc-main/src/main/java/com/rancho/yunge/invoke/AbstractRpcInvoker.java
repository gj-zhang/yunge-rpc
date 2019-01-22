package com.rancho.yunge.invoke;

import com.rancho.yunge.context.ContextHolder;

public abstract class AbstractRpcInvoker implements RpcInvoker {

    ContextHolder contextHolder;

    public AbstractRpcInvoker(ContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }


}