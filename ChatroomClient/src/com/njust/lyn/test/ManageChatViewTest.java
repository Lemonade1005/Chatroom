package com.njust.lyn.test;

import com.njust.lyn.client.service.ManageChatView;
import com.njust.lyn.client.view.ChatView;
import com.njust.lyn.common.Message;

/**
 * @author lyn
 * @date 2022/9/8 10:47
 * 测试用来管理聊天界面的类
 */
public class ManageChatViewTest {
    public static void main(String[] args) {
        Message message = new Message();
        message.setSender("1");
        message.setGetter("2");
        ChatView chatView = new ChatView("1", "2");
        ManageChatView.addChatView("12", chatView);
        ChatView chatView2 = ManageChatView.getChatView("12");
        chatView2.showMessage(message);
    }
}
