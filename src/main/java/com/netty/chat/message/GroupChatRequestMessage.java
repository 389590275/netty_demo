package com.netty.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:09 下午
 */
@Data
@ToString(callSuper = true)
public class GroupChatRequestMessage extends Message {

    private String username;
    private String groupName;
    private String content;

    public GroupChatRequestMessage(String username, String groupName, String content) {
        this.username = username;
        this.groupName = groupName;
        this.content = content;
    }

    public GroupChatRequestMessage() {
    }

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
}
