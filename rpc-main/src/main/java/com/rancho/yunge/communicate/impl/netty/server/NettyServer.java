package com.rancho.yunge.communicate.impl.netty.server;

import com.rancho.yunge.communicate.AbstractServer;
import com.rancho.yunge.context.ContextHolder;
import com.rancho.yunge.exception.StartUpException;
import com.rancho.yunge.provider.DefaultRpcProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer extends AbstractServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private DefaultRpcProvider rpcProvider;
    private Thread serverThread;

    public NettyServer() {
    }

    @Override
    public void start() {
        Runnable runnable = () -> {

        };
        serverThread = new Thread(runnable, "Netty-Server-Thread");
        serverThread.setDaemon(true);
        serverThread.start();
        if (serverThread.isAlive()) {
            onStart();
        } else {
            logger.error("netty server start error, server thread is not alive");
            throw new StartUpException("netty server start error, server thread is not alive.");
        }
    }

    @Override
    public void stop() {

        try {
            if (serverThread != null && serverThread.isAlive()) {
                serverThread.interrupt();
            }
        } finally {
            onStop();
        }

    }

    @Override
    public void setup(ContextHolder contextHolder) {
        this.rpcProvider = new DefaultRpcProvider(contextHolder);
    }


}
