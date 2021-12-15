package com.netty.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author xiangchijie
 * @date 2021/12/15 4:21 下午
 */
@Data
@ToString(callSuper = true)
public class GroupJoinRequestMessage extends Message {

    private String userName;
    private String groupName;

    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }

    public GroupJoinRequestMessage() {
    }

    public GroupJoinRequestMessage(String userName,String groupName) {
        this.userName = userName;
        this.groupName = groupName;
    }

}
