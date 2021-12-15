package com.netty.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author xiangchijie
 * @date 2021/12/15 10:02 上午
 */
@Data
@ToString(callSuper = true)
public class LoginRequestMessage extends Message {

    private String username;
    private String password;

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }

    @Override
    public int getSequenceId() {
        return 0;
    }

}
