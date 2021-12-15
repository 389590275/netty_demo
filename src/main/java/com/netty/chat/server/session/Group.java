package com.netty.chat.server.session;

import lombok.Data;

import java.util.Set;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:34 下午
 */
@Data
public class Group {

    private String groupName;

    private Set<String> members;

    public Group(String groupName, Set<String> members) {
        this.groupName = groupName;
        this.members = members;
    }
}
