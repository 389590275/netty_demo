package com.netty.chat.protocol;

import com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 用于扩展序列化算法
 *
 * @author xiangchijie
 * @date 2021/12/20 3:32 下午
 */
public interface Serializer {

    /**
     * 反序列化方法
     *
     * @param clazz
     * @param bytes
     * @param <T>
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    /**
     * 序列化方法
     *
     * @param object
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T object);

    enum Algorithm implements Serializer {

        Java {
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                return SerializationUtils.deserialize(bytes);
            }

            @Override
            public <T> byte[] serialize(T object) {
                return SerializationUtils.serialize((Serializable) object);
            }
        },

        Json {

            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                String value = new String(bytes, StandardCharsets.UTF_8);
                return new Gson().fromJson(value, clazz);
            }

            @Override
            public <T> byte[] serialize(T object) {
                return new Gson().toJson(object).getBytes(StandardCharsets.UTF_8);
            }

        }


    }

}
