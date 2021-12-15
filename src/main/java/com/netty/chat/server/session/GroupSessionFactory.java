package com.netty.chat.server.session;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:34 下午
 */
public class GroupSessionFactory {

    public static final GroupSession groupSession = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return groupSession;
    }

}
