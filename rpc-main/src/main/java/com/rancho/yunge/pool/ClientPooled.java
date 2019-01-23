package com.rancho.yunge.pool;

import com.rancho.yunge.invoker.RpcInvokerFactory;
import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.serializer.Serializer;
import com.rancho.yunge.util.NetUtil;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 11:50
 */
public abstract class ClientPooled {

    protected static transient Logger logger = LoggerFactory.getLogger(ClientPooled.class);


    // ---------------------- iface ----------------------

    public abstract void init(String host, int port, final Serializer serializer, final RpcInvokerFactory rpcInvokerFactory) throws Exception;

    public abstract void close();

    public abstract boolean isValidate();

    public abstract void send(RpcRequestWrapper requestWrapper) throws Exception ;


    // ---------------------- client pool map ----------------------

    private static ConcurrentHashMap<String, GenericObjectPool<ClientPooled>> clientPoolMap;        // (static) alread addStopCallBack
    public static GenericObjectPool<ClientPooled> getPool(String address, Class<? extends ClientPooled> clientPoolImpl,
                                                          Serializer serializer, final RpcInvokerFactory rpcInvokerFactory) throws Exception {

        // init client-pool-map, avoid repeat init
        if (clientPoolMap == null) {
            synchronized (ClientPooled.class) {
                if (clientPoolMap == null) {
                    // init
                    clientPoolMap = new ConcurrentHashMap<>();
                    // stop callback
                    rpcInvokerFactory.addStopCallBack(() -> {
                            if (clientPoolMap.size() > 0) {
                                for (String key:clientPoolMap.keySet()) {
                                    GenericObjectPool<ClientPooled> clientPool = clientPoolMap.get(key);
                                    clientPool.close();
                                }
                                clientPoolMap.clear();
                            }
                        }
                    );
                }
            }
        }

        // get pool
        GenericObjectPool<ClientPooled> clientPool = clientPoolMap.get(address);
        if (clientPool != null) {
            return clientPool;
        }

        // make pool, avoid repeat init
        synchronized (clientPoolMap) {

            // re-get pool
            clientPool = clientPoolMap.get(address);
            if (clientPool != null) {
                return clientPool;
            }

            // parse address
            Object[] array = NetUtil.parseIpPort(address);
            String host = (String) array[0];
            int port = (int) array[1];

            // set pool
            clientPool = new GenericObjectPool<ClientPooled>(new ClientPoolFactory(clientPoolImpl, host, port, serializer, rpcInvokerFactory));
            clientPool.setTestOnBorrow(true);
            clientPool.setMaxTotal(2);

            clientPoolMap.put(address, clientPool);

            return clientPool;
        }

    }

}
