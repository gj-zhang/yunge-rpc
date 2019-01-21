package com.rancho.yunge.provider;

import com.rancho.yunge.context.ContextHolder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRpcProvider implements RpcProvider {

    ContextHolder contextHolder;

    public AbstractRpcProvider(ContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }


}