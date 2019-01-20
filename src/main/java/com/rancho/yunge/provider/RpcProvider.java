package com.rancho.yunge.provider;

import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.params.RpcResponseWrapper;

import java.util.Map;

public interface RpcProvider {

    void addService(String key, Object rpcService);

    Object getService(String key);

    Map<String, Object> getAllService();

    String makeServiceKey(String iface, String version);

    RpcResponseWrapper invokeService(RpcRequestWrapper requestWrapper);

}
