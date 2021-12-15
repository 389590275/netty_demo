package com.netty.chat.message;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:09 下午
 */
@Data
@ToString(callSuper = true)
public class GroupCreateRequestMessage extends Message {

    private String groupName;
    private Set<String> members;

    public GroupCreateRequestMessage(String groupName, Set<String> members) {
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }
}
