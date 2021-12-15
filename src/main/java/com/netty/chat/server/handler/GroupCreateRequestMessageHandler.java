package com.netty.chat.server.handler;

import com.netty.chat.message.GroupCreateRequestMessage;
import com.netty.chat.message.GroupCreateResponseMessage;
import com.netty.chat.server.session.Group;
import com.netty.chat.server.session.GroupSession;
import com.netty.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:43 下午
 */
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        // 群管理器
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (group != null) {
            // 发送拉群消息
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupCreateResponseMessage(true, "你已被拉入" + groupName));
            }
            // 发送成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));
        } else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "创建失败"));
        }

    }

}
