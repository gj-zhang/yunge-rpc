package com.rancho.yunge.invoke;

import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.params.RpcResponseWrapper;

public interface RpcInvoker {

    RpcResponseWrapper invokeService(RpcRequestWrapper requestWrapper);

}
