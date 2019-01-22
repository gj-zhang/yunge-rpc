package com.rancho.yunge.registry;

import java.util.Map;
import java.util.TreeSet;

public abstract class AbstractRegistry implements Registry {

    protected Map<String, String> registryParam;

    protected Map<String, TreeSet<String>> registryData;

    @Override
    public void init(Map<String, String> params) {
        this.registryParam = params;
    }

    @Override
    public void stop() {
        registryData.clear();
    }
}
