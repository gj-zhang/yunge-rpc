package com.rancho.yunge.provider;

import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.params.RpcResponseWrapper;

import java.util.Map;

public interface RpcProvider {

    RpcResponseWrapper invokeService(RpcRequestWrapper requestWrapper);

}
