package com.netty.source;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author xiangchijie
 * @date 2022/1/4 3:36 下午
 */
public class TestSourceServer {

    public static void main(String[] args) {
        String[] names = {"大哲", "如意", "嘉恒"};
        ArrayList<String> service = Lists.newArrayList("pub", "container", "info");
        Collections.shuffle(service);
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + "  " + service.get(i));
        }
        System.out.println("项驰捷  match");
    }

}
