package com.rancho.yunge.sample.client;

import com.rancho.yunge.communicate.CommunicateType;
import com.rancho.yunge.context.ClientContextHolder;
import com.rancho.yunge.demo.protocol.UserProtocol;
import com.rancho.yunge.demo.service.UserService;
import com.rancho.yunge.invoker.RpcInvokerFactory;
import com.rancho.yunge.invoker.call.CallType;
import com.rancho.yunge.loadBalance.LoadBalance;
import com.rancho.yunge.serializer.AbstractSerializer;

import java.util.concurrent.TimeUnit;

/**
 * @author rancho
 * @createTime 2019-01-25 16:14
 */
public class ClientApplicationSample {

    public static void main(String[] args) throws Exception {
        ClientContextHolder clientContextHolder = new ClientContextHolder(CommunicateType.NETTY,
                AbstractSerializer.SerializerEnum.JSON.getSerializer(), CallType.ONEWAY, LoadBalance.RANDOM, UserService.class,
                "", 100, "10.49.6.208:8081", "", null, null);
        UserService userService = (UserService) clientContextHolder.getObject();
        UserProtocol rancho = userService.getUserInfo("rancho");
        System.out.println(rancho);


        TimeUnit.SECONDS.sleep(2);

        RpcInvokerFactory.getInstance().stop();
    }

}
