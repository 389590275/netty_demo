package com.netty.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:22 下午
 */
@Data
@ToString(callSuper = true)
public class GroupQuitRequestMessage extends Message {

    private String username;
    private String groupName;

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }

    public GroupQuitRequestMessage(String username, String groupName) {
        this.username = username;
        this.groupName = groupName;
    }
    
}
