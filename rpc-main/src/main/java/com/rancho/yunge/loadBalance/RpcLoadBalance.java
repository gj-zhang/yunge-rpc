package com.rancho.yunge.loadBalance;

import java.util.TreeSet;

/**
 * @author zgj-18063794
 * @createTime 2019-01-23 11:00
 */
public interface RpcLoadBalance {

    String route(String serviceKye, TreeSet<String> addressSet);

}
