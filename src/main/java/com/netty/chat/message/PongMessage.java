package com.netty.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author xiangchijie
 * @date 2021/12/20 2:59 下午
 */
@Data
@ToString(callSuper = true)
public class PongMessage extends AbstractResponseMessage {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
