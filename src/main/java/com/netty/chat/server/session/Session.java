package com.netty.chat.server.session;


import io.netty.channel.Channel;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:26 下午
 */
public interface Session {

    /**
     * 绑定会话
     *
     * @param channel
     * @param username
     */
    void bind(Channel channel, String username);

    void unBind(Channel channel);

    Object getAttribute(Channel channel, String name);

    void setAttribute(Channel channel, String name, Object value);

    Channel getChannel(String username);

}
