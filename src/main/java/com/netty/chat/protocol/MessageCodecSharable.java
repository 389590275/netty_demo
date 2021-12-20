package com.netty.chat.protocol;

import com.netty.chat.config.Config;
import com.netty.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;

/**
 * 必须和LengthFieldBasedFrameDecoder一起用。确保
 *
 * @author xiangchijie
 * @date 2021/12/15 2:14 下午
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) {
        ByteBuf out = ctx.alloc().buffer();
        // 把msg写入out
        // 1. 4字节的魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1字节的版本
        out.writeByte(1);
        // 3. 1字节序列化方式 jdk 0 json 1
        int ordinal = Config.getSerializerAlgorithm().ordinal();
        out.writeByte(ordinal);
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
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
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
