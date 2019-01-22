package com.rancho.yunge.context;

import com.rancho.yunge.communicate.CommunicateType;
import com.rancho.yunge.communicate.Server;
import com.rancho.yunge.configuration.YunGeConfiguration;
import com.rancho.yunge.exception.BindPortException;
import com.rancho.yunge.func.CallBack;
import com.rancho.yunge.registry.Registry;
import com.rancho.yunge.serializer.Serializer;
import com.rancho.yunge.util.NetUtil;
import com.rancho.yunge.util.PreCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultContextHolder implements ContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DefaultContextHolder.class);


    private Serializer serializer;
    private CommunicateType communicateType;
    private String ip;
    private Integer port;
    private Class<? extends Registry> registryClass;
    private Registry registry;
    private Map<String, String> registryParams;
    private Server server;

    protected Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    public DefaultContextHolder() {
    }

    //todo get all config entry from config file，support spring boot
/*    public void init() {

    }*/

    public void init(Serializer serializer, CommunicateType communicateType,
                     String ip, Integer port, Class<? extends Registry> registryClass,
                     Map<String, String> registryParams) throws BindPortException {
        this.serializer = serializer;
        this.communicateType = communicateType;
        this.ip = ip;
        this.port = port;
        this.registryClass = registryClass;
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
    public String makeServiceKey(String iface, String version) {
        return iface + "#" + version;
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
    public void start() {
        try {
            String address = NetUtil.getIpAndPort(ip, port);
            CallBack startCallBack = () -> {
                if (registryClass == null) {
                    return;
                }
                registry = registryClass.newInstance();
                registry.init(registryParams);
                registry.start();
                if (serviceMap.size() > 0) {
                    registry.registry(serviceMap.keySet(), address);
                }
            };
            CallBack stopCallBack = () -> {
                if (registry != null) {
                    if (serviceMap.size() > 0) {
                        registry.remove(serviceMap.keySet(), address);
                    }
                    registry.stop();
                    registry = null;
                }
            };
            server = communicateType.serverClass.newInstance();
            server.setStartCallBack(startCallBack);
            server.setStopCallBack(stopCallBack);
            server.setup(this);
            server.start();
        } catch (InstantiationException e) {
            //todo 异常处理
            logger.error("can not instance server " + communicateType.serverClass.getName());
        } catch (IllegalAccessException e) {
            //todo 异常处理
            logger.error("can not instance server " + communicateType.serverClass.getName());
        }
    }

    @Override
    public void stop() {
        server.stop();
    }

    @Override
    public YunGeConfiguration getConfiguration() throws IOException {
        return new YunGeConfiguration();
    }

    /*public static class Builder {
        private Serializer serializer;
        private CommunicateType communicateType;
        private String ip;
        private Integer port;
        private Class<? extends Registry> registryClass;
        private Map<String, String> registryParams;

        public Builder setSerializer(Serializer serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder setCommunicateType(CommunicateType communicateType) {
            this.communicateType = communicateType;
            return this;
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(Integer port) {
            this.port = port;
            return this;
        }

        public Builder setRegistryClass(Class<? extends Registry> registryClass) {
            this.registryClass = registryClass;
            return this;
        }

        public Builder setRegistryParams(Map<String, String> registryParams) {
            this.registryParams = registryParams;
            return this;
        }

        public ContextHolder build() {
            DefaultContextHolder defaultContextHolder = new DefaultContextHolder();
            defaultContextHolder.ip = this.ip;
            defaultContextHolder.port = this.port;
            defaultContextHolder.serializer = this.serializer;
            defaultContextHolder.communicateType = this.communicateType;
            defaultContextHolder.registryClass = this.registryClass;
            defaultContextHolder.registryParams = this.registryParams;
            return defaultContextHolder;
        }
    }*/
}
