package com.rancho.yunge.communicate;

import com.rancho.yunge.func.CallBack;
import com.rancho.yunge.lifecycle.AbstractLifeCycle;
import com.rancho.yunge.lifecycle.LifeCycleStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServer extends AbstractLifeCycle implements Server {

    private static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    private String ip;
    private Integer port;

    private CallBack startCallBack;

    private CallBack stopCallBack;


    public void setStartCallBack(CallBack startCallBack) {
        this.startCallBack = startCallBack;
    }

    public void setStopCallBack(CallBack stopCallBack) {
        this.stopCallBack = stopCallBack;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public void onStart() {
        startCallBack.run();
        setCurrentState(LifeCycleStateEnum.LIVE);
    }

    @Override
    public void onStop() {
        stopCallBack.run();
        setCurrentState(LifeCycleStateEnum.DEAD);
    }
}
