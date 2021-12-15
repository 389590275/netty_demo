package com.netty.chat.server.session;

import io.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:47 下午
 */
public class GroupSessionMemoryImpl implements GroupSession {

    private static final Map<String, Group> GROUP_MAP = new ConcurrentHashMap<>();

    @Override
    public Group createGroup(String name, Set<String> members) {
        if (GROUP_MAP.containsKey(name)) {
            return null;
        }
        Group group = new Group(name, members);
        GROUP_MAP.put(name, group);
        return group;
    }

    @Override
    public Group joinMember(String name, String member) {
        Group group = GROUP_MAP.get(name);
        if (group == null) {
            return null;
        }
        group.getMembers().add(member);
        return group;
    }

    @Override
    public Group removerMember(String name, String member) {
        Group group = GROUP_MAP.get(name);
        if (group == null) {
            return null;
        }
        group.getMembers().remove(member);
        return group;
    }

    @Override
    public Group removeGroup(String name) {
        return GROUP_MAP.remove(name);
    }

    @Override
    public Set<String> getMembers(String name) {
        Group group = GROUP_MAP.get(name);
        if (group == null) {
            return new HashSet<>();
        }
        return group.getMembers();
    }

    @Override
    public List<Channel> getMembersChannel(String name) {
        List<Channel> channels = new ArrayList<>();
        Set<String> members = getMembers(name);
        for (String member : members) {
            Optional.ofNullable(SessionFactory.getSession().getChannel(member)).ifPresent(channels::add);
        }
        return channels;
    }

}
