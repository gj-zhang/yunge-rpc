package com.rancho.yunge.provider;

import com.rancho.yunge.communicate.CommunicateType;
import com.rancho.yunge.registry.Registry;
import com.rancho.yunge.serializer.Serializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRpcProvider implements RpcProvider {

    protected CommunicateType communicateType;

    protected Serializer serializer;

    protected String ip;
    protected Integer port;

    protected Class<? extends Registry> registryClass;
    protected Map<String, String> registryParam;

    protected Map<String, Object> serviceMap = new ConcurrentHashMap<>();



}