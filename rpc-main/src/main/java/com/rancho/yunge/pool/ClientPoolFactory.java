package com.rancho.yunge.pool;

import com.rancho.yunge.invoker.RpcInvokerFactory;
import com.rancho.yunge.serializer.Serializer;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 11:54
 */
public class ClientPoolFactory extends BasePooledObjectFactory<ClientPooled> {


    private Class<? extends ClientPooled> clientPoolImpl;

    private String host;
    private int port;
    private Serializer serializer;
    private RpcInvokerFactory rpcInvokerFactory;


    public ClientPoolFactory(Class<? extends ClientPooled> clientPoolImpl, String host, int port,
                             final Serializer serializer, final RpcInvokerFactory rpcInvokerFactory) {
        this.clientPoolImpl = clientPoolImpl;
        this.host = host;
        this.port = port;
        this.serializer = serializer;
        this.rpcInvokerFactory = rpcInvokerFactory;
    }

    @Override
    public ClientPooled create() throws Exception {
        ClientPooled clientPooled = clientPoolImpl.newInstance();
        clientPooled.init(host, port, serializer, rpcInvokerFactory);

        return clientPooled;
    }

    @Override
    public PooledObject<ClientPooled> wrap(ClientPooled clientPooled) {
        return new DefaultPooledObject<>(clientPooled);
    }

    @Override
    public void destroyObject(PooledObject<ClientPooled> p) throws Exception {
        ClientPooled clientPooled = p.getObject();
        clientPooled.close();
    }

    @Override
    public boolean validateObject(PooledObject<ClientPooled> p) {
        ClientPooled clientPooled = p.getObject();
        return clientPooled.isValidate();
    }
}
