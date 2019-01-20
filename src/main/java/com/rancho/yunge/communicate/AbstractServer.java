package com.rancho.yunge.communicate;

import com.rancho.yunge.func.CallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    private CallBack startCallBack;

    private CallBack stopCallBack;

    public void setStartCallBack(CallBack startCallBack) {
        this.startCallBack = startCallBack;
    }

    public void setStopCallBack(CallBack stopCallBack) {
        this.stopCallBack = stopCallBack;
    }

}
