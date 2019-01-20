package com.rancho.yunge.serializer.impl;

import com.rancho.yunge.serializer.Serializer;

public class JsonSerializer implements Serializer {
    @Override
    public byte[] serializer(Object o) {
        return new byte[0];
    }

    @Override
    public Object deserializer(byte[] bytes) {
        return null;
    }
}
