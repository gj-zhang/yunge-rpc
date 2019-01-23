package com.rancho.yunge.invoker.callback;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 11:20
 */
public abstract class RpcInvokerCallBack<T> {

    public abstract void onSuccess(T result);

    public abstract void OnFailure(Throwable throwable);

    private static ThreadLocal<RpcInvokerCallBack> threadLocalCallBack = new ThreadLocal<>();

    public static RpcInvokerCallBack getCallback() {
        RpcInvokerCallBack rpcInvokerCallBack = threadLocalCallBack.get();
        threadLocalCallBack.remove();
        return rpcInvokerCallBack;
    }

    public static void setCallBack(RpcInvokerCallBack callBack) {
        threadLocalCallBack.set(callBack);
    }

    public static void remove() {
        threadLocalCallBack.remove();
    }


}
