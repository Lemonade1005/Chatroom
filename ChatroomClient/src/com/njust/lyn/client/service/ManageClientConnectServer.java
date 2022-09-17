package com.njust.lyn.client.service;

import java.util.HashMap;

/**
 * @author lyn
 * @date 2022/9/2 20:34
 * 使用集合管理客户端连接到服务端的进程
 */
public class ManageClientConnectServer {
    //将多个线程放入一个HashMap集合中,注意格式
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();  //这个HashMap集合作为属性,设为static方便直接调用

    //得到集合
    public static HashMap<String, ClientConnectServerThread> getHm() {
        return hm;
    }

    //通过userId从HashMap集合中得到进程
    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return hm.get(userId);  //HashMap 的get() 方法
    }

    //通过userId向HashMap集合中添加进程
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);  //HashMap 的put() 方法
    }

    //通过userId从HashMap集合中移除进程
    public static void removeClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.remove(userId, clientConnectServerThread);  //HashMap 的remove() 方法
    }
}
