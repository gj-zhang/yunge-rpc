package com.rancho.yunge.communicate.impl.netty.client;

import com.rancho.yunge.invoker.RpcInvokerFactory;
import com.rancho.yunge.params.RpcResponseWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 11:58
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponseWrapper> {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    private RpcInvokerFactory invokerFactory;

    public NettyClientHandler(RpcInvokerFactory invokerFactory) {
        this.invokerFactory = invokerFactory;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("rpc netty client caught exception", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseWrapper msg) throws Exception {
        invokerFactory.notifyInvokerFuture(msg.getRequestId(), msg);
    }
}
