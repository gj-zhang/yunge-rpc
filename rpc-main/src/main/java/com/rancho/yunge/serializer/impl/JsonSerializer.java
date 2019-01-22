package com.rancho.yunge.serializer.impl;

import com.rancho.yunge.serializer.Serializer;

public class JsonSerializer<T> implements Serializer<T> {
    @Override
    public byte[] serializer(T o) {
        return new byte[0];
    }

    @Override
    public T deserializer(byte[] bytes, Class<T> tClass) {
        return null;
    }
}
