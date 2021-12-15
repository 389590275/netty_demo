package com.netty.chat.server.handler;

import com.netty.chat.message.GroupCreateRequestMessage;
import com.netty.chat.message.GroupJoinRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:43 下午
 */
@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {

    }

}
