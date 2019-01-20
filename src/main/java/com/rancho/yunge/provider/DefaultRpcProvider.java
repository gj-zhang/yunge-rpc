package com.rancho.yunge.provider;

import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.params.RpcResponseWrapper;

import java.util.HashMap;
import java.util.Map;

public class DefaultRpcProvider extends AbstractRpcProvider {


    @Override
    public void addService(String key, Object rpcService) {
        serviceMap.put(key, rpcService);
    }

    @Override
    public Object getService(String key) {
        return serviceMap.get(key);
    }

    @Override
    public Map<String, Object> getAllService() {
        return new HashMap<>(serviceMap);
    }

    @Override
    public String makeServiceKey(String iface, String version) {
        return iface + "&" + version;
    }

    @Override
    public RpcResponseWrapper invokeService(RpcRequestWrapper requestWrapper) {
        RpcResponseWrapper response = new RpcResponseWrapper();

        response.setRequestId(requestWrapper.getRequestId());
        String serviceKey = makeServiceKey(requestWrapper.getClassName(), requestWrapper.getVersion());
        Object service = serviceMap.get(serviceKey);
        if (service == null) {
            response.setErrorMsg("can not found service " + requestWrapper.getClassName() + ", version " + requestWrapper.getVersion());
            return response;
        }

        if (System.currentTimeMillis() - requestWrapper.getCreateTime() > 3 * 60 * 1000 ) {
            response.setErrorMsg("the time between create and execute greater than limits 3 * 60 * 1000");
        }




        return null;
    }

}
