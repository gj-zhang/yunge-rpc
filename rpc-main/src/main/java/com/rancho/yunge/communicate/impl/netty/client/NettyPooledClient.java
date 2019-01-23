package com.rancho.yunge.communicate.impl.netty.client;

import com.rancho.yunge.communicate.impl.netty.codec.NettyDecoder;
import com.rancho.yunge.communicate.impl.netty.codec.NettyEncoder;
import com.rancho.yunge.invoker.RpcInvokerFactory;
import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.params.RpcResponseWrapper;
import com.rancho.yunge.pool.ClientPooled;
import com.rancho.yunge.serializer.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 11:50
 */
public class NettyPooledClient extends ClientPooled {


    private EventLoopGroup group;
    private Channel channel;


    @Override
    public void init(String host, int port, Serializer serializer, RpcInvokerFactory rpcInvokerFactory) throws Exception {
        this.group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new NettyEncoder(RpcRequestWrapper.class, serializer))
                                .addLast(new NettyDecoder(RpcResponseWrapper.class, serializer))
                                .addLast(new NettyClientHandler(rpcInvokerFactory));
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
        this.channel = bootstrap.connect(host, port).sync().channel();

        // valid
        if (!isValidate()) {
            close();
            return;
        }

        logger.debug("rpc netty client proxy, connect to server success at host:{}, port:{}", host, port);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isValidate() {
        return false;
    }

    @Override
    public void send(RpcRequestWrapper requestWrapper) throws Exception {

    }
}
