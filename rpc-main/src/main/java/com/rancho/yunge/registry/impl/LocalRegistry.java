package com.rancho.yunge.registry.impl;

import com.rancho.yunge.registry.AbstractRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LocalRegistry extends AbstractRegistry {

    @Override
    public void init(Map<String, String> params) {
        super.init(params);
    }

    @Override
    public void start() {
        //do nothing
    }

    @Override
    public void stop() {
        super.stop();
    }

    /**
     * {@link com.rancho.yunge.registry.Registry#registry(Set, String)}
     */
    @Override
    public boolean registry(Set<String> keys, String address) {
        if (keys == null || keys.isEmpty() || address == null || address.trim().length() == 0) {
            return false;
        }
        for (String key : keys) {
            TreeSet<String> addresses = registryData.computeIfAbsent(key, k -> new TreeSet<>());
            addresses.add(address);
        }
        return true;
    }

    /**
     * {@link com.rancho.yunge.registry.Registry#remove(Set, String)}
     */
    @Override
    public String remove(Set<String> keys, String address) {
        if (keys == null || keys.isEmpty() || address == null || address.trim().length() == 0) {
            return null;
        }

        for (String key : keys) {
            TreeSet<String> addresses = registryData.computeIfAbsent(key, k -> new TreeSet<>());
            addresses.remove(address);
        }
        return address;
    }

    @Override
    public Map<String, TreeSet<String>> discovery(Set<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return null;
        }
        Map<String, TreeSet<String>> tmp = new HashMap<>();
        for (String key : keys) {
            TreeSet<String> discovery = discovery(key);
            if (discovery != null) {
                tmp.put(key, discovery);
            }
        }
        return tmp;
    }

    @Override
    public TreeSet<String> discovery(String key) {
        return registryData.get(key);
    }
}
