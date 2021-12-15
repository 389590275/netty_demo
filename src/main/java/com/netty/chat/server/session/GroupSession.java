package com.netty.chat.server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:34 下午
 */
public interface GroupSession {

    /**
     * 创建一个聊天组，组不存在才能创建，否则返回null
     *
     * @param name
     * @param members
     * @return
     */
    Group createGroup(String name, Set<String> members);

    /**
     * 加入聊天组
     *
     * @param name   组名
     * @param member 成员名
     * @return 如果组不存在返回null
     */
    Group joinMember(String name, String member);

    /**
     * 移除组成员
     *
     * @param name   组名
     * @param member 成员名
     * @return 如果组不存在返回null
     */
    Group removerMember(String name, String member);


    /**
     * 移除聊天组
     *
     * @param name
     * @return 如果组不存在返回null
     */
    Group removeGroup(String name);

    /**
     * 获取组成员
     *
     * @param name
     * @return 成员集合，没有策划稿能源会返回empty set
     */
    Set<String> getMembers(String name);


    /**
     * 获取组成员的channel集合，只有在线的channel才会返回
     *
     * @param name
     * @return
     */
    List<Channel> getMembersChannel(String name);

}
