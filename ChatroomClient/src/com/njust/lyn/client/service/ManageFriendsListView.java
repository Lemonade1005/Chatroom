package com.njust.lyn.client.service;

import com.njust.lyn.client.view.FriendsListView;

import java.util.HashMap;

/**
 * @author lyn
 * @date 2022/9/8 14:31
 * 管理各个用户的好友列表,方便展示在线用户
 */
public class ManageFriendsListView {  //每个 userId 对应一个 FriendsList 对象
    private static HashMap<String, FriendsListView> hm = new HashMap<>();  //key: 请求显示在线好友列表的userId, value:根据在线好友列表更新过后的好友列表

    //添加每个用户对应的好友列表
    public static void addFriendsList(String userId, FriendsListView friendsListView) {  //参数: 用户Id, 好友列表
        hm.put(userId, friendsListView);
    }

    //根据用户Id得到其好友列表
    public static FriendsListView getFriendsList(String userId) {
        return hm.get(userId);
    }
}
