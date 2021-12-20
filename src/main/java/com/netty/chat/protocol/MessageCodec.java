package com.netty.chat.protocol;

import com.netty.chat.config.Config;
import com.netty.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xiangchijie
 * @date 2021/12/15 9:45 上午
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 把msg写入out
        // 1. 4字节的魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1字节的版本
        out.writeByte(1);
        // 3. 1字节序列化方式 jdk 0 json 1
        out.writeByte(Config.getSerializerAlgorithm().ordinal());
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
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        // 找到反序列化算法
        Serializer.Algorithm algorithm = Serializer.Algorithm.values()[serializerType];
        // 确定具体消息类型
        Class<?> messageClass = Message.getMessageClass(messageType);
        Object msg = algorithm.deserialize(messageClass, bytes);
        log.debug("{} {} {} {} {} {} {} ", magicNum, version, serializerType, messageType, sequenceId, length, msg);
        log.debug("{}", msg);
        out.add(msg);
    }

}
