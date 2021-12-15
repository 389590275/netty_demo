package com.netty.chat.server.handler;

import com.netty.chat.server.session.Session;
import com.netty.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiangchijie
 * @date 2021/12/15 5:22 下午
 */
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {

    /**
     * 断开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = SessionFactory.getSession();
        Channel channel = ctx.channel();
        session.unBind(channel);
        log.debug("{} 已经断开", channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Session session = SessionFactory.getSession();
        Channel channel = ctx.channel();
        session.unBind(channel);
        log.error("{} 异常断开", channel, cause);
    }

}
