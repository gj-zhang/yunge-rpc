package com.rancho.yunge.context;

import com.rancho.yunge.communicate.Client;
import com.rancho.yunge.communicate.CommunicateType;
import com.rancho.yunge.exception.YunGeRpcException;
import com.rancho.yunge.invoker.RpcInvokerFactory;
import com.rancho.yunge.invoker.call.CallType;
import com.rancho.yunge.invoker.callback.RpcInvokerCallBack;
import com.rancho.yunge.loadBalance.LoadBalance;
import com.rancho.yunge.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 10:51
 */
public class ClientContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(ClientContextHolder.class);

    private CommunicateType communicateType;
    private Serializer serializer;
    private CallType callType;
    private LoadBalance loadBalance;
    private Class<?> iface;
    private String version;
    private long timeout = 1000;

    private String address;
    private String accessToken;
    private RpcInvokerCallBack callBack;
    private RpcInvokerFactory invokerFactory;

    public ClientContextHolder(CommunicateType communicateType,
                               Serializer serializer,
                               CallType callType,
                               LoadBalance loadBalance,
                               Class<?> iface,
                               String version,
                               long timeout,
                               String address,
                               String accessToken,
                               RpcInvokerCallBack invokeCallback,
                               RpcInvokerFactory invokerFactory
    ) {

        this.communicateType = communicateType;
        this.serializer = serializer;
        this.callType = callType;
        this.loadBalance = loadBalance;
        this.iface = iface;
        this.version = version;
        this.timeout = timeout;
        this.address = address;
        this.accessToken = accessToken;
        this.callBack = invokeCallback;
        this.invokerFactory = invokerFactory;

        // valid
        if (this.communicateType==null) {
            throw new YunGeRpcException("ClientContextHolder communicateType missing.");
        }
        if (this.serializer==null) {
            throw new YunGeRpcException("ClientContextHolder serializer missing.");
        }
        if (this.callType==null) {
            throw new YunGeRpcException("ClientContextHolder callType missing.");
        }
        if (this.loadBalance==null) {
            throw new YunGeRpcException("ClientContextHolder loadBalance missing.");
        }
        if (this.iface==null) {
            throw new YunGeRpcException("ClientContextHolder iface missing.");
        }
        if (this.timeout < 0) {
            this.timeout = 0;
        }
        if (this.invokerFactory == null) {
            this.invokerFactory = RpcInvokerFactory.getInstance();
        }

        // init Client
        initClient();
    }

    public Serializer getSerializer() {
        return serializer;
    }
    public long getTimeout() {
        return timeout;
    }

    public RpcInvokerFactory getInvokerFactory() {
        return invokerFactory;
    }

    Client client = null;

    private void initClient() {
        try {
            client = communicateType.clientClass.newInstance();
            client.init(this);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new YunGeRpcException(e);
        }
    }


    public Object getObject() {
        Object o = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{iface}, (proxy, method, args) -> {
                    String className = method.getDeclaringClass().getName();
                    if (Object.class.getName().equals(className)) {
                        logger.info("rpc proxy class-method not support [{}.{}]", className, method.getName());
                        throw new YunGeRpcException("rpc proxy class-method not support");
                    }

                    String finalAddress = address;
                    if (finalAddress == null || finalAddress.trim().length() == 0) {

                    }

                    return null;
                });

        return o;
    }

}
