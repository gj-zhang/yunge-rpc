package com.rancho.yunge.communicate;

import com.rancho.yunge.context.ClientContextHolder;
import com.rancho.yunge.params.RpcRequestWrapper;

public interface Client {

    void init(ClientContextHolder clientContextHolder);

    void asyncSend(String address, RpcRequestWrapper requestWrapper) throws Exception;

}
