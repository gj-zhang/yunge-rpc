package com.rancho.yunge.registry;

import com.rancho.yunge.lifecycle.LifeCycle;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public interface Registry extends LifeCycle {

    /**
     * 注册参数
     * @param params
     */
    void init(Map<String, String> params);

    /**
     * 注册
     * @param keys 接口名称+版本 set
     * @param address ip:port
     * @return 是否成功
     */
    boolean registry(Set<String> keys, String address);


    String remove(Set<String> keys, String address);

    Map<String, TreeSet<String>> discovery(Set<String> keys);

    TreeSet<String> discovery(String key);

}
