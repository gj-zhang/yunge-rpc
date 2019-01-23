package com.rancho.yunge.invoker;

import com.rancho.yunge.exception.YunGeRpcException;
import com.rancho.yunge.func.CallBack;
import com.rancho.yunge.params.RpcFutureResponse;
import com.rancho.yunge.params.RpcResponseWrapper;
import com.rancho.yunge.registry.AbstractRegistry;
import com.rancho.yunge.registry.Registry;
import com.rancho.yunge.registry.impl.LocalRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 11:25
 */
public class RpcInvokerFactory {

    private static Logger logger = LoggerFactory.getLogger(RpcInvokerFactory.class);

    private static volatile RpcInvokerFactory instance = new RpcInvokerFactory(LocalRegistry.class, null);

    private Class<? extends AbstractRegistry> serviceRegistryClass;
    private Map<String, String> serviceRegistryParam;

    private Registry serviceRegistry;

    public RpcInvokerFactory() {
    }

    public RpcInvokerFactory(Class<? extends AbstractRegistry> serviceRegistryClass, Map<String, String> serviceRegistryParam) {
        this.serviceRegistryClass = serviceRegistryClass;
        this.serviceRegistryParam = serviceRegistryParam;
    }

    public static RpcInvokerFactory getInstance() {
        return instance;
    }

    public void start() throws Exception {
        // start registry
        if (serviceRegistryClass != null) {
            serviceRegistry = serviceRegistryClass.newInstance();
            serviceRegistry.init(serviceRegistryParam);
            serviceRegistry.start();
        }
    }

    public Registry getServiceRegistry() {
        return serviceRegistry;
    }

    private List<CallBack> stopCallbackList = new ArrayList<>();

    public void addStopCallBack(CallBack callback) {
        stopCallbackList.add(callback);
    }


    private ConcurrentMap<String, RpcFutureResponse> futureResponsePool = new ConcurrentHashMap<>();

    public void setInvokerFuture(String requestId, RpcFutureResponse response) {
        futureResponsePool.put(requestId, response);
    }

    public void removeInvokerFuture(String requestId) {
        futureResponsePool.remove(requestId);
    }

    public void notifyInvokerFuture(String requestId, RpcResponseWrapper response) {
        RpcFutureResponse rpcFutureResponse = futureResponsePool.get(requestId);
        if (rpcFutureResponse == null) return;

        if (rpcFutureResponse.getInvokerCallBack() != null) {
            executeResponseCallback(() -> {
                try {
                    if (response.getErrorMsg() != null) {
                        rpcFutureResponse.getInvokerCallBack().OnFailure(new YunGeRpcException(response.getErrorMsg()));
                    } else {
                        rpcFutureResponse.getInvokerCallBack().onSuccess(response.getResult());
                    }
                } catch (Exception e) {
                    logger.error("execute call back error", e);
                }
            });
        } else {
            rpcFutureResponse.setResponseWrapper(response);
        }

        futureResponsePool.remove(requestId);
    }

    private ThreadPoolExecutor responseCallBackExecutorPool = null;

    public void executeResponseCallback(Runnable runnable) {
        if (responseCallBackExecutorPool == null) {
            synchronized (this) {
                if (responseCallBackExecutorPool == null) {
                    responseCallBackExecutorPool = new ThreadPoolExecutor(
                            10,
                            100,
                            60L,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(1000),
                            (r, executor) -> {
                                throw new YunGeRpcException("rpc Invoke Callback Thread pool is EXHAUSTED!");
                            });        // default maxThreads 300, minThreads 60
                }
            }
        }
        responseCallBackExecutorPool.execute(runnable);
    }


    public void stop() throws Exception {
        // stop registry
        if (serviceRegistry != null) {
            serviceRegistry.stop();
        }

        // stop callback
        if (stopCallbackList.size() > 0) {
            for (CallBack callback : stopCallbackList) {
                try {
                    callback.run();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        // stop CallbackThreadPool
        stopCallbackThreadPool();
    }

    private void stopCallbackThreadPool() {
        if (responseCallBackExecutorPool != null && !responseCallBackExecutorPool.isShutdown()) {
            responseCallBackExecutorPool.shutdown();
        }
    }


}
