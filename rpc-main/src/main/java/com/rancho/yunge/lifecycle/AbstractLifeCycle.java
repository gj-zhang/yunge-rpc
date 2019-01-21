package com.rancho.yunge.lifecycle;

/**
 * @author zgj-18063794
 * @createTime 2019-01-21 14:00
 */
public abstract class AbstractLifeCycle implements LifeCycle {

    private LifeCycleStateEnum currentState;

    @Override
    public LifeCycleStateEnum getCurrentState() {
        return currentState;
    }

    @Override
    public void setCurrentState(LifeCycleStateEnum currentState) {
        this.currentState = currentState;
    }
}
