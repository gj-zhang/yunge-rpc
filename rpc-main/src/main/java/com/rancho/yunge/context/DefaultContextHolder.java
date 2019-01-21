package com.rancho.yunge.context;

import com.rancho.yunge.communicate.CommunicateType;
import com.rancho.yunge.exception.BindPortException;
import com.rancho.yunge.exception.StartUpException;
import com.rancho.yunge.lifecycle.LifeCycle;
import com.rancho.yunge.registry.Registry;
import com.rancho.yunge.serializer.Serializer;
import com.rancho.yunge.util.NetUtil;
import com.rancho.yunge.util.PreCondition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultContextHolder implements ContextHolder {

    private Serializer serializer;
    private CommunicateType communicateType;
    private String ip;
    private Integer port;
    private Registry registry;
    private Map<String, String> registryParams;
    private LifeCycle lifeCycle;

    protected Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    DefaultContextHolder() {
    }


    public void init(Serializer serializer, CommunicateType communicateType,
                     String ip, Integer port, Registry registry,
                     Map<String, String> registryParams) throws BindPortException {
        this.serializer = serializer;
        this.communicateType = communicateType;
        this.ip = ip;
        this.port = port;
        this.registry = registry;
        this.registryParams = registryParams;
        valid();

    }

    private void valid() throws BindPortException {
        PreCondition.check(serializer == null, "serializer can not be null");
        PreCondition.check(communicateType == null, "communicate type can not be null");
        if (ip == null) {
            ip = NetUtil.getLocalAddress();
        }
        if (port <= 0) {
            port = 8081;
        }
        if (NetUtil.isPortUsed(port)) {
            throw new BindPortException("can not bind port " + port + ", port is in used");
        }

        if (registry != null ) {
            if (registryParams == null) {
                throw new IllegalArgumentException("registry param is miss");
            }
        }
    }

    @Override
    public void addService(String iface, String version, Object rpcService) {
        String key = makeServiceKey(iface, version);
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
        return iface + "#" + version;
    }

    @Override
    public Serializer getSerializer() {
        return serializer;
    }

    @Override
    public CommunicateType getCommunicateType() {
        return communicateType;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public Registry getRegistry() {
        return registry;
    }

    @Override
    public Map<String, String> getRegistryParams() {
        return registryParams;
    }

    @Override
    public void refresh() throws Exception {
        lifeCycle = communicateType.serverClass.newInstance();
        lifeCycle.start();
    }
}
