package com.rancho.yunge.communicate;

import com.rancho.yunge.communicate.impl.netty.client.NettyClient;
import com.rancho.yunge.communicate.impl.netty.server.NettyServer;

public enum CommunicateType {

    NETTY(NettyServer.class, NettyClient.class);

    public final Class<? extends AbstractServer> serverClass;
    public final Class<? extends AbstractClient> clientClass;

    CommunicateType(Class<? extends AbstractServer> serverClass, Class<? extends AbstractClient> clientClass) {
        this.serverClass = serverClass;
        this.clientClass = clientClass;
    }


}
