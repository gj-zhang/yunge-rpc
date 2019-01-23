package com.rancho.yunge.communicate.impl.netty.client;

import com.rancho.yunge.communicate.AbstractClient;
import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.pool.ClientPooled;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);


    @Override
    public void asyncSend(String address, RpcRequestWrapper requestWrapper) throws Exception {
        // client pool	[tips03 : may save 35ms/100invoke if move it to constructor, but it is necessary. cause by ConcurrentHashMap.get]
        GenericObjectPool<ClientPooled> clientPool = ClientPooled.getPool(address, NettyPooledClient.class,
                clientContextHolder.getSerializer(), clientContextHolder.getInvokerFactory());
        // client proxt
        ClientPooled clientPoolProxy = null;

        try {
            // proxy borrow
            clientPoolProxy = clientPool.borrowObject();

            // do invoke
            clientPoolProxy.send(requestWrapper);
        } catch (Exception e) {
            throw e;
        } finally{
            // proxy return
            if (clientPoolProxy != null) {
                clientPool.returnObject(clientPoolProxy);
            }
        }

    }
}
