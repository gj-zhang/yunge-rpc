package com.rancho.yunge.communicate.impl.netty.server;

import com.rancho.yunge.communicate.AbstractServer;
import com.rancho.yunge.communicate.impl.netty.codec.NettyDecoder;
import com.rancho.yunge.communicate.impl.netty.codec.NettyEncoder;
import com.rancho.yunge.configuration.ThreadFactoryBuilder;
import com.rancho.yunge.configuration.YunGeConfiguration;
import com.rancho.yunge.context.ContextHolder;
import com.rancho.yunge.exception.StartUpException;
import com.rancho.yunge.exception.YunGeRpcException;
import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.params.RpcResponseWrapper;
import com.rancho.yunge.provider.DefaultRpcProvider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NettyServer extends AbstractServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private DefaultRpcProvider rpcProvider;
    private ContextHolder contextHolder;
    //todo all config from configuration class
    private YunGeConfiguration configuration;
    private Thread serverThread;

    public NettyServer() {
    }

    @Override
    public void start() {
        Runnable runnable = () -> {
            final ThreadPoolExecutor serverHandlerPool = new ThreadPoolExecutor(
                    60, 300, 60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(10000), ((r, executor) -> {
                throw new YunGeRpcException("rpc netty server handler pool is exhausted!");
            }));
            serverHandlerPool.setThreadFactory(new ThreadFactoryBuilder());
            EventLoopGroup parentGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {

                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(parentGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline()
                                        .addLast(new NettyDecoder(RpcRequestWrapper.class, contextHolder.getSerializer()))
                                        .addLast(new NettyEncoder(RpcResponseWrapper.class, contextHolder.getSerializer()))
                                        .addLast(new ServerHandler(rpcProvider, serverHandlerPool));
                            }
                        })
                        .option(ChannelOption.SO_TIMEOUT, 100)
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                logger.info("netty server start success, port = {}", contextHolder.getPort());
                onStart();

                ChannelFuture future = serverBootstrap.bind(contextHolder.getPort()).sync();

                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                logger.error("netty server stop.", e);
            } catch (Exception e) {
                logger.error("netty server error.", e);
            } finally {
                try {
                    serverHandlerPool.shutdown();
                    workerGroup.shutdownGracefully();
                    parentGroup.shutdownGracefully();
                } catch (Exception e) {
                    logger.error("close resource error.", e);
                }
            }
        };
        serverThread = new Thread(runnable, "Netty-Server-Thread");
        serverThread.setDaemon(true);
        serverThread.start();
        if (!serverThread.isAlive()) {
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
            try {
                onStop();
            } catch (Exception e) {
                logger.error("stop call back error");
            }
        }

    }

    @Override
    public void setup(ContextHolder contextHolder) {
        this.contextHolder = contextHolder;
        this.rpcProvider = new DefaultRpcProvider(contextHolder);
        try {
            this.configuration = contextHolder.getConfiguration();
        } catch (IOException e) {
            logger.error("can not parse config file");
        }
    }


}
