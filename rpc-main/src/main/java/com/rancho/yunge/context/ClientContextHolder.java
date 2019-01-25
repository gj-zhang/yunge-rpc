package com.rancho.yunge.context;

import com.rancho.yunge.communicate.Client;
import com.rancho.yunge.communicate.CommunicateType;
import com.rancho.yunge.exception.YunGeRpcException;
import com.rancho.yunge.invoker.RpcInvokerFactory;
import com.rancho.yunge.invoker.call.CallType;
import com.rancho.yunge.invoker.call.RpcInvokerFuture;
import com.rancho.yunge.invoker.callback.RpcInvokerCallBack;
import com.rancho.yunge.loadBalance.LoadBalance;
import com.rancho.yunge.params.RpcFutureResponse;
import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.params.RpcResponseWrapper;
import com.rancho.yunge.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
        if (this.communicateType == null) {
            throw new YunGeRpcException("ClientContextHolder communicateType missing.");
        }
        if (this.serializer == null) {
            throw new YunGeRpcException("ClientContextHolder serializer missing.");
        }
        if (this.callType == null) {
            throw new YunGeRpcException("ClientContextHolder callType missing.");
        }
        if (this.loadBalance == null) {
            throw new YunGeRpcException("ClientContextHolder loadBalance missing.");
        }
        if (this.iface == null) {
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
                        if (invokerFactory != null && invokerFactory.getServiceRegistry() != null) {
                            String serviceKey = iface.getName() + "#" + version;
                            TreeSet<String> addressSet = invokerFactory.getServiceRegistry().discovery(serviceKey);
                            if (addressSet == null || addressSet.size() == 0) {
                                //pass
                            } else if (addressSet.size() == 1) {
                                finalAddress = addressSet.first();
                            } else {
                                finalAddress = loadBalance.loadBalance.route(serviceKey, addressSet);
                            }
                        }
                    }
                    if (finalAddress == null || finalAddress.trim().length() == 0) {
                        throw new YunGeRpcException("rpc client context holder service [" + className + "] address empty");
                    }
                    RpcRequestWrapper request = new RpcRequestWrapper();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setClassName(className);
                    request.setMethodName(method.getName());
                    request.setCreateTime(System.currentTimeMillis());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);
                    if (CallType.SYNC == callType) {
                        return syncCall(finalAddress, request);
                    } else if (CallType.FUTURE == callType) {
                        return futureCall(finalAddress, request);
                    } else if (CallType.CALLBACK == callType) {
                        callBackCall(finalAddress, request);
                        return null;
                    } else if (CallType.ONEWAY == callType) {
                        client.asyncSend(finalAddress, request);
                        return null;
                    } else {
                        throw new YunGeRpcException("unsupported call type [" + callType + "]");
                    }
                });

        return o;
    }

    private void callBackCall(String finalAddress, RpcRequestWrapper request) throws Exception {
        RpcInvokerCallBack finalInvokeCallBack = this.callBack;
        RpcInvokerCallBack threadLocalCallback = RpcInvokerCallBack.getCallback();
        if (threadLocalCallback != null) {
            finalInvokeCallBack = threadLocalCallback;
        }
        if (finalInvokeCallBack == null) {
            throw new YunGeRpcException("rpc invoker call back (callType=)" + CallType.CALLBACK.name() + ") cannot be null");
        }
        RpcFutureResponse futureResponse = new RpcFutureResponse(invokerFactory, request, finalInvokeCallBack);
        try {
            client.asyncSend(finalAddress, request);
        } catch (Exception e) {
            logger.info("rpc invoke error, address: {}, rpc request={}", finalAddress, request);
            futureResponse.removeInvokerFuture();
            throw e;
        }
    }

    private Object futureCall(String finalAddress, RpcRequestWrapper request) throws Exception {
        RpcFutureResponse futureResponse = new RpcFutureResponse(invokerFactory, request, null);
        try {
            RpcInvokerFuture invokerFuture = new RpcInvokerFuture(futureResponse);
            RpcInvokerFuture.setFuture(invokerFuture);
            client.asyncSend(finalAddress, request);
            return null;
        } catch (Exception e) {
            logger.info("rpc invoker error, address: {}, request={}", finalAddress, request);
            futureResponse.removeInvokerFuture();
            throw e;
        }
    }

    private Object syncCall(String finalAddress, RpcRequestWrapper request) throws Exception {
        RpcFutureResponse futureResponse = new RpcFutureResponse(invokerFactory, request, null);
        try {
            client.asyncSend(finalAddress, request);
            RpcResponseWrapper responseWrapper = futureResponse.get(timeout, TimeUnit.MILLISECONDS);
            if (responseWrapper.getErrorMsg() != null) {
                throw new YunGeRpcException(responseWrapper.getErrorMsg());
            }
            return responseWrapper.getResult();
        } catch (Exception e) {
            logger.info("rpc invoke error, address: {}, rpc request={}", finalAddress, request);
            throw e;
        } finally {
            futureResponse.removeInvokerFuture();
        }
    }

}
