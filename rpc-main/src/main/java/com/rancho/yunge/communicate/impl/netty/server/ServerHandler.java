package com.rancho.yunge.communicate.impl.netty.server;

import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.params.RpcResponseWrapper;
import com.rancho.yunge.provider.RpcProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zgj-18063794
 * @createTime 2019-01-22 10:41
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequestWrapper> {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private RpcProvider rpcProvider;
    private ThreadPoolExecutor serverHandlerPool;

    public ServerHandler(RpcProvider rpcProvider, ThreadPoolExecutor serverHandlerPool) {
        this.rpcProvider = rpcProvider;
        this.serverHandlerPool = serverHandlerPool;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestWrapper msg) {
        serverHandlerPool.execute(() -> {
            RpcResponseWrapper responseWrapper;
            try {
                responseWrapper = rpcProvider.invokeService(msg);
                ctx.writeAndFlush(responseWrapper);
            } catch (Exception e) {
                responseWrapper = new RpcResponseWrapper();
                responseWrapper.setRequestId(msg.getRequestId());
                responseWrapper.setErrorMsg(e.getMessage());
                ctx.writeAndFlush(responseWrapper);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("rpc netty server caught exception", cause);
        ctx.close();
    }
}
