package com.rancho.yunge.sample.server.impl;

import com.rancho.yunge.demo.protocol.UserProtocol;
import com.rancho.yunge.demo.service.UserService;

/**
 * @author zgj-18063794
 * @createTime 2019-01-22 15:29
 */
public class UserServiceImpl implements UserService {

    @Override
    public UserProtocol getUserInfo(String name) {
        UserProtocol userProtocol = new UserProtocol();
        userProtocol.setName("rancho");
        userProtocol.setAge(18);
        userProtocol.setGender("man");
        userProtocol.setHandsome(true);
        return userProtocol;
    }
}
