package com.netty.chat.server.service;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:23 下午
 */
public interface UserService {

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    boolean login(String username, String password);


}
