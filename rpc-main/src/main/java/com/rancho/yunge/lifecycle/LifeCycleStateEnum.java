package com.rancho.yunge.lifecycle;

/**
 * @author zgj-18063794
 * @createTime 2019-01-21 13:57
 */
public enum LifeCycleStateEnum {
    LIVE(0, "Started"),
    DEAD(1, "STOP");
    private Integer code;
    private String state;

    LifeCycleStateEnum(Integer code, String state) {
        this.code = code;
        this.state = state;
    }
}
