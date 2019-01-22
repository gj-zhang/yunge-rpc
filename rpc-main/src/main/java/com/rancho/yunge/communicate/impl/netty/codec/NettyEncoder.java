package com.rancho.yunge.communicate.impl.netty.codec;

import com.rancho.yunge.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author zgj-18063794
 * @createTime 2019-01-22 10:41
 */
public class NettyEncoder extends MessageToByteEncoder<Object> {

    private Serializer serializer;
    private Class<?> genericClass;

    public NettyEncoder(Class<?> genericClass, Serializer serializer) {
        this.serializer = serializer;
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (genericClass.isInstance(msg)) {
            byte[] data = this.serializer.serializer(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
