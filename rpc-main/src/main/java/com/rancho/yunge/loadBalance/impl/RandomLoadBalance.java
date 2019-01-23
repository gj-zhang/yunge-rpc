package com.rancho.yunge.loadBalance.impl;

import com.rancho.yunge.loadBalance.RpcLoadBalance;

import java.util.Random;
import java.util.TreeSet;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 11:01
 */
public class RandomLoadBalance implements RpcLoadBalance {

    Random random = new Random();

    @Override
    public String route(String serviceKye, TreeSet<String> addressSet) {
        String[] strings = addressSet.toArray(new String[addressSet.size()]);
        String address = strings[random.nextInt(addressSet.size())];
        return address;
    }
}
