package com.netty.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:09 下午
 */
@Data
@ToString(callSuper = true)
public class GroupMembersRequestMessage extends Message {

    private String groupName;

    @Override
    public int getMessageType() {
        return GroupMembersRequestMessage;
    }

    public GroupMembersRequestMessage() {
    }

    public GroupMembersRequestMessage(String groupName) {
        this.groupName = groupName;
    }

}
