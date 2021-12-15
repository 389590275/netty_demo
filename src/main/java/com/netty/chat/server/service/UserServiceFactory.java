package com.netty.chat.server.service;

/**
 * @author xiangchijie
 * @date 2021/12/15 3:14 下午
 */
public class UserServiceFactory {

    public static final UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }

}
