package com.rancho.yunge.serializer;

import com.rancho.yunge.exception.YunGeRpcException;
import com.rancho.yunge.serializer.impl.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zgj-18063794
 * @createTime 2019-01-22 15:45
 */
public abstract class AbstractSerializer implements Serializer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSerializer.class);

    public enum SerializerEnum {
        JSON(JsonSerializer.class);
        private Class<? extends Serializer> serializerClass;

        SerializerEnum(Class<? extends Serializer> serializerClass) {
            this.serializerClass = serializerClass;
        }

        public Serializer getSerializer() {
            try {
                return serializerClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("instance serializer class error", e);
                throw new YunGeRpcException(e);
            }
        }
    }

}
