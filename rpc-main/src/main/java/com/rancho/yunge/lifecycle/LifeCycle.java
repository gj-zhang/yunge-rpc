package com.rancho.yunge.lifecycle;

import com.rancho.yunge.context.ContextHolder;

public interface LifeCycle {

    void start();

    void stop();

    void setup(ContextHolder contextHolder);

    LifeCycleStateEnum getCurrentState();

    void setCurrentState(LifeCycleStateEnum currentState);

}
