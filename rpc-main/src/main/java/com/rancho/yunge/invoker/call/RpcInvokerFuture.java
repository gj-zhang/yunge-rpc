package com.rancho.yunge.invoker.call;

import com.rancho.yunge.exception.YunGeRpcException;
import com.rancho.yunge.params.RpcFutureResponse;
import com.rancho.yunge.params.RpcResponseWrapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author rancho
 * @createTime 2019-01-25 15:13
 */
public class RpcInvokerFuture implements Future {

    private RpcFutureResponse futureResponse;

    public RpcInvokerFuture(RpcFutureResponse futureResponse) {
        this.futureResponse = futureResponse;
    }

    public void stop() {
        futureResponse.removeInvokerFuture();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return futureResponse.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return futureResponse.isCancelled();
    }

    @Override
    public boolean isDone() {
        return futureResponse.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        try {
            return futureResponse.get(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new YunGeRpcException(e);
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            RpcResponseWrapper responseWrapper = futureResponse.get(timeout, unit);
            if (responseWrapper.getErrorMsg() != null) {
                throw new YunGeRpcException(responseWrapper.getErrorMsg());
            }
            return responseWrapper.getResult();
        } finally {
            stop();
        }
    }

    private static ThreadLocal<RpcInvokerFuture> futureThreadLocal = new ThreadLocal<>();

    public static <T> Future<T> getFuture(Class<T> type) {
        Future<T> rpcInvokerFuture = (Future<T>) futureThreadLocal.get();
        futureThreadLocal.remove();
        return rpcInvokerFuture;
    }

    public static void setFuture(RpcInvokerFuture future) {
        futureThreadLocal.set(future);
    }

    public static void removeFuture() {
        futureThreadLocal.remove();
    }
}
