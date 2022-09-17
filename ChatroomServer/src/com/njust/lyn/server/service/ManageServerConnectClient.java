package com.njust.lyn.server.service;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author lyn
 * @date 2022/9/2 21:36
 * 使用集合管理服务端连接到客户端的线程
 */
public class ManageServerConnectClient {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();  //这个HashMap集合作为属性,设为static,只有一个HashMap且方便直接调用

    //得到在线用户线程的集合
    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //通过userId从HashMap集合中得到线程
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return hm.get(userId);  //HashMap 的get() 方法
    }

    //通过userId向HashMap集合中添加线程
    public static void addServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);  //HashMap 的put() 方法
    }

    //通过userId从HashMap集合中移除线程
    public static void removeServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.remove(userId, serverConnectClientThread);  //HashMap 的remove() 方法
    }

    //返回在线用户列表
    public static String getOnlineUserList(){
        String onlineUserList = "";  //在线用户列表
        //使用迭代器遍历存放在集合中的已启动的线程,即正在与客户端通信的线程
        Iterator<String> iterator = hm.keySet().iterator();  //存放线程的集合的key为String类型的userId
        while (iterator.hasNext()){
            onlineUserList += iterator.next() + " "; //用空格分开
        }
        return onlineUserList;
    }

    //返回在线用户数组
    public static String[] getOnlineUserArray() {
        String onlineUser[] = hm.keySet().toArray(new String[hm.size()]);  //将集合中的key存在数组中,即在线的用户Id
        return onlineUser;
    }
}
