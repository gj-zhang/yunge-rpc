package com.rancho.yunge.registry.impl;

import com.rancho.yunge.registry.AbstractRegistry;

import java.util.Set;

public class LocalRegistry extends AbstractRegistry {
    @Override
    public boolean registry(Set<String> keys, String value) {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setup() {

    }
}
