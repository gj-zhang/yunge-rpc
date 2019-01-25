package com.rancho.yunge.sample.server;

import com.rancho.yunge.communicate.CommunicateType;
import com.rancho.yunge.context.ContextHolder;
import com.rancho.yunge.context.DefaultContextHolder;
import com.rancho.yunge.demo.service.UserService;
import com.rancho.yunge.exception.BindPortException;
import com.rancho.yunge.sample.server.impl.UserServiceImpl;
import com.rancho.yunge.serializer.AbstractSerializer;

import java.util.concurrent.TimeUnit;

/**
 * @author zgj-18063794
 * @createTime 2019-01-22 15:19
 */
public class ServerApplicationSample {

    public static void main(String[] args) throws InterruptedException, BindPortException {
        DefaultContextHolder defaultInstance = new DefaultContextHolder();
        defaultInstance.init(
                AbstractSerializer.SerializerEnum.JSON.getSerializer(),
                CommunicateType.NETTY,
                "", 0, null, null);
        defaultInstance.addService(UserService.class.getName(), "1.0.0", new UserServiceImpl());
        try {
            defaultInstance.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!Thread.currentThread().isInterrupted()) {
            TimeUnit.HOURS.sleep(1);
        }


    }

}
