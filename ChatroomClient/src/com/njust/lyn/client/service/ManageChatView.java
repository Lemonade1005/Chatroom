package com.njust.lyn.client.service;

import com.njust.lyn.client.view.ChatView;

import java.util.HashMap;

/**
 * @author lyn
 * @date 2022/9/7 21:30
 * 使用集合管理聊天界面,以便于将消息内容显示在对应的聊天窗口内
 */
public class ManageChatView {
    private static HashMap<String, ChatView> hm = new HashMap<>();  //key:发送者和接收者的Id组合(唯一标识), value:聊天界面

    //加入聊天界面(在好友列表时点击好友头像时用到)
    public static void addChatView(String userId_friendId, ChatView chatView){
        hm.put(userId_friendId, chatView);
    }

    //得到聊天界面(在收到服务端回复消息时用到)
    public static ChatView getChatView(String userId_friendId){
        return hm.get(userId_friendId);  //根据 key 得到 value
    }
}
