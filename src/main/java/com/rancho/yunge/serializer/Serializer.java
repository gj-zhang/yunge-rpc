package com.rancho.yunge.serializer;

public interface Serializer<T> {

    byte[] serializer(T t);

    T deserializer(byte[] bytes);
}
