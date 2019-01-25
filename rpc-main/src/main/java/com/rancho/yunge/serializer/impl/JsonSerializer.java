package com.rancho.yunge.serializer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rancho.yunge.exception.YunGeRpcException;
import com.rancho.yunge.serializer.Serializer;

import java.io.IOException;

public class JsonSerializer<T> implements Serializer<T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serializer(T o) {
        try {
            return objectMapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw new YunGeRpcException(e);
        }
    }

    @Override
    public T deserializer(byte[] bytes, Class<T> tClass) {
        try {
            return objectMapper.readValue(bytes, tClass);
        } catch (IOException e) {
            throw new YunGeRpcException(e);
        }
    }
}
