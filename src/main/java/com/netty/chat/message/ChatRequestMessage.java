package com.netty.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:06 下午
 */
@Data
@ToString(callSuper = true)
public class ChatRequestMessage extends Message {

    private String from;
    private String to;
    private String content;

    public ChatRequestMessage(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public ChatRequestMessage() {
    }


    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }

}
