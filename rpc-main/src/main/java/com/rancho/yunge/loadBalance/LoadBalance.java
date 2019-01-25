package com.rancho.yunge.loadBalance;

import com.rancho.yunge.loadBalance.impl.RandomLoadBalance;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 10:58
 */
public enum LoadBalance {
    RANDOM(new RandomLoadBalance());

    public RpcLoadBalance loadBalance;

    LoadBalance(RpcLoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }
}
