package com.netty.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:09 下午
 */
@Data
@ToString(callSuper = true)
public class GroupChatResponseMessage extends AbstractResponseMessage {

    private String from;
    private String content;

    public GroupChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public GroupChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
}
