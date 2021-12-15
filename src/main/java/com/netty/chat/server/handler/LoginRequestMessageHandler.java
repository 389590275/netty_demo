package com.netty.chat.server.handler;

import com.netty.chat.message.LoginRequestMessage;
import com.netty.chat.message.LoginResponseMessage;
import com.netty.chat.server.service.UserServiceFactory;
import com.netty.chat.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:31 下午
 */
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean success = UserServiceFactory.getUserService().login(username, password);
        // 登录成功
        LoginResponseMessage message;
        if (success) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            message = new LoginResponseMessage(true, "登录成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码错误");
        }
        ctx.writeAndFlush(message);
    }
}
