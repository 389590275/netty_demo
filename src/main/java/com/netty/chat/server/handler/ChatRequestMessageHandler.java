package com.netty.chat.server.handler;

import com.netty.chat.message.ChatRequestMessage;
import com.netty.chat.message.ChatResponseMessage;
import com.netty.chat.server.session.Session;
import com.netty.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:31 下午
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Session session = SessionFactory.getSession();
        Channel channel = session.getChannel(to);
        // 对方在线
        if (channel != null) {
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        } else {
            channel.writeAndFlush(new ChatResponseMessage(false,"对方不存在或者不在线"));
        }
    }

}
