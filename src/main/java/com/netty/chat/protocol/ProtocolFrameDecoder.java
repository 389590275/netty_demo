package com.netty.chat.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:50 下午
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolFrameDecoder() {
        super(1024, 12, 4, 0, 0);
    }

}
