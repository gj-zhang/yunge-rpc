package com.rancho.yunge.context;

import com.rancho.yunge.communicate.CommunicateType;
import com.rancho.yunge.registry.Registry;
import com.rancho.yunge.serializer.Serializer;

import java.util.Map;

public interface ContextHolder {


    void addService(String iface, String version, Object rpcService);


    Object getService(String key);

    /**
     * @return not thread safe map
     */
    Map<String, Object> getAllService();

    String makeServiceKey(String iface, String version);

    Serializer getSerializer();

    CommunicateType getCommunicateType();

    String getIp();

    Integer getPort();

    Registry getRegistry();

    Map<String, String> getRegistryParams();

    void refresh() throws Exception;

}
