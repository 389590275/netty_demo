package com.netty.chat.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:24 下午
 */
public class UserServiceMemoryImpl implements UserService {

    private Map<String, String> allUserMap = new ConcurrentHashMap<>();

    {
        allUserMap.put("zhangsan", "123");
        allUserMap.put("lisi", "123");
        allUserMap.put("wangwu", "123");
        allUserMap.put("zhaoliu", "123");
        allUserMap.put("qianqi", "123");
    }

    @Override
    public boolean login(String username, String password) {
        return password.equals(allUserMap.get(username));
    }

}
