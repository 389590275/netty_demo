package com.netty.chat.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiangchijie
 * @date 2021/12/15 9:44 上午
 */
public abstract class Message implements Serializable {

    public static Class<?> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    public int getSequenceId() {
        return 0;
    }


    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMembersRequestMessage = 12;
    public static final int GroupMembersResponseMessage = 13;
    public static final int PingMessage = 14;
    public static final int PongMessage = 15;
    private static final Map<Integer, Class<?>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(LoginRequestMessage,LoginRequestMessage.class);
        messageClasses.put(LoginResponseMessage,LoginResponseMessage.class);
        messageClasses.put(ChatRequestMessage,ChatRequestMessage.class);
        messageClasses.put(GroupCreateRequestMessage,GroupCreateRequestMessage.class);
        messageClasses.put(GroupCreateResponseMessage,GroupCreateResponseMessage.class);
        messageClasses.put(GroupJoinRequestMessage,GroupJoinRequestMessage.class);
        messageClasses.put(GroupQuitRequestMessage,GroupQuitRequestMessage.class);
        messageClasses.put(GroupChatResponseMessage,GroupChatResponseMessage.class);
        messageClasses.put(GroupMembersRequestMessage,GroupMembersRequestMessage.class);
        messageClasses.put(PingMessage,PingMessage.class);
        messageClasses.put(PongMessage,PongMessage.class);

    }

}
