package com.netty.chat.server.handler;

import com.netty.chat.message.GroupChatRequestMessage;
import com.netty.chat.message.GroupChatResponseMessage;
import com.netty.chat.server.session.GroupSession;
import com.netty.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:43 下午
 */
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        List<Channel> channels = groupSession.getMembersChannel(groupName);
        GroupChatResponseMessage message = new GroupChatResponseMessage(msg.getUsername(), msg.getContent());
        for (Channel channel : channels) {
            channel.writeAndFlush(message);
        }
    }

}
