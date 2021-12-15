package com.netty.chat.server.session;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:26 下午
 */
public class SessionFactory {

    private static final Session session = new SessionMemoryImpl();

    public static Session getSession(){
        return session;
    }


}
