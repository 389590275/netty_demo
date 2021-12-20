package com.netty.chat.protocol;

import com.netty.chat.config.Config;
import com.netty.chat.message.LoginRequestMessage;
import com.netty.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author xiangchijie
 * @date 2021/12/20 3:58 下午
 */
public class TestSerializer {

    public static void main(String[] args) {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOG = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOG,CODEC,LOG);
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");

//        channel.writeOutbound(message);
        channel.writeInbound(messageToByteBuf(message));
    }

    public static ByteBuf messageToByteBuf(Message msg){
        int algorithm = Config.getSerializerAlgorithm().ordinal();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        // 把msg写入out
        // 1. 4字节的魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1字节的版本
        out.writeByte(1);
        // 3. 1字节序列化方式 jdk 0 json 1
        out.writeByte(algorithm);
        // 4. 1字节指令类型
        out.writeByte(msg.getMessageType());
        // 5. 4字节请求序号
        out.writeInt(msg.getSequenceId());
        // 无意义 对齐填充
        out.writeByte(0xff);
        // 6. 4字节正文长度
        byte[] bytes = Config.getSerializerAlgorithm().serialize(msg);
        out.writeInt(bytes.length);
        // 7. bytes.length的正文内容
        out.writeBytes(bytes);
        return out;
    }

}
