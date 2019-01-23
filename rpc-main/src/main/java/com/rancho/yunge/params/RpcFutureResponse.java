package com.rancho.yunge.params;

import com.rancho.yunge.exception.YunGeRpcException;
import com.rancho.yunge.invoker.RpcInvokerFactory;
import com.rancho.yunge.invoker.callback.RpcInvokerCallBack;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 11:30
 */
public class RpcFutureResponse implements Future<RpcResponseWrapper> {

    private RpcInvokerFactory invokerFactory;

    private RpcRequestWrapper requestWrapper;
    private RpcResponseWrapper responseWrapper;


    private boolean done = false;
    private final Object lock = new Object();

    private RpcInvokerCallBack invokerCallBack;

    public RpcFutureResponse(RpcInvokerFactory invokerFactory, RpcRequestWrapper requestWrapper, RpcInvokerCallBack invokerCallBack) {
        this.invokerFactory = invokerFactory;
        this.requestWrapper = requestWrapper;
        this.invokerCallBack = invokerCallBack;
        setInvokerFuture();
    }

    private void setInvokerFuture() {
        this.invokerFactory.setInvokerFuture(requestWrapper.getRequestId(), this);
    }

    public void removeInvokerFuture() {
        this.invokerFactory.removeInvokerFuture(this.requestWrapper.getRequestId());
    }

    public RpcRequestWrapper getRequestWrapper() {
        return requestWrapper;
    }

    public RpcInvokerCallBack getInvokerCallBack() {
        return invokerCallBack;
    }

    public void setResponseWrapper(RpcResponseWrapper responseWrapper) {
        this.responseWrapper = responseWrapper;
        synchronized (lock) {
            done = true;
            lock.notify();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public RpcResponseWrapper get() throws InterruptedException, ExecutionException {

        try {
            return get(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new YunGeRpcException(e);
        }
    }

    @Override
    public RpcResponseWrapper get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!done) {
            synchronized (lock) {
                try {
                    if (timeout < 0) {
                        lock.wait();
                    } else {
                        long timeMillis = (TimeUnit.MILLISECONDS == unit)? timeout: TimeUnit.MILLISECONDS.convert(timeout, unit);
                        lock.wait(timeMillis);
                    }
                } catch (InterruptedException e) {
                    throw e;
                }
            }
        }
        if (!done) {
            throw new YunGeRpcException("rpc request timeout at:" + System.currentTimeMillis() + ", request:" + requestWrapper.toString());
        }
        return responseWrapper;
    }
}
