package com.netty.chat.config;

import com.netty.chat.protocol.Serializer;

/**
 * @author xiangchijie
 * @date 2021/12/20 3:46 下午
 */
public class Config {


    public static Serializer.Algorithm getSerializerAlgorithm() {
        return Serializer.Algorithm.Json;
    }

}
