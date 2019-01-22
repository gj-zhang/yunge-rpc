package com.rancho.yunge.lifecycle;

import com.rancho.yunge.context.ContextHolder;

public interface ApplicationContextLifeCycle extends LifeCycle {

    void setup(ContextHolder contextHolder);

    LifeCycleStateEnum getCurrentState();

    void setCurrentState(LifeCycleStateEnum currentState);
}
