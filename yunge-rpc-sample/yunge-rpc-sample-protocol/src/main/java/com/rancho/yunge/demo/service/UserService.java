package com.rancho.yunge.demo.service;

import com.rancho.yunge.demo.protocol.UserProtocol;

/**
 * @author zgj-18063794
 * @createTime 2019-01-22 15:29
 */
public interface UserService {

    UserProtocol getUserInfo(String name);

}
