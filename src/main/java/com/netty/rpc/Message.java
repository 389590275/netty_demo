package com.netty.rpc;

import com.netty.chat.message.LoginRequestMessage;

import java.io.Serializable;
import java.lang.reflect.Proxy;


/**
 * @author xiangchijie
 * @date 2021/12/31 10:20 上午
 */
public abstract class Message implements Serializable {

    public static void main(String[] args) {
        LoginRequestMessage message = new LoginRequestMessage("", "");

        Proxy.newProxyInstance(message.getClass().getClassLoader(), new Class<?>[]{message.getClass()}, (proxy, method, arg) -> {
            
                    return proxy;
                }
        );

    }


}
