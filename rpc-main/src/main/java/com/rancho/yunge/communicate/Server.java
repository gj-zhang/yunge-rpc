package com.rancho.yunge.communicate;

import com.rancho.yunge.lifecycle.LifeCycle;

public interface Server extends LifeCycle {

    String getIp();

    void setIp(String ip);

    Integer getPort();

    void setPort(Integer port);

    void onStart();

    void onStop();

}
