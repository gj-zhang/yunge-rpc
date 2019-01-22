package com.rancho.yunge.communicate;

import com.rancho.yunge.func.CallBack;
import com.rancho.yunge.lifecycle.ApplicationContextLifeCycle;

public interface Server extends ApplicationContextLifeCycle {

    String getIp();

    void setIp(String ip);

    Integer getPort();

    void setPort(Integer port);

    void onStart() throws Exception;

    void onStop() throws Exception;

    void setStartCallBack(CallBack startCallBack);

    void setStopCallBack(CallBack stopCallBack);

}
