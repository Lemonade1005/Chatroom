package com.njust.lyn.client.service;

import com.njust.lyn.client.view.ChatView;
import com.njust.lyn.client.view.FriendsListView;
import com.njust.lyn.common.FileMessage;
import com.njust.lyn.common.Message;
import com.njust.lyn.common.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author lyn
 * @date 2022/9/2 19:36
 * 该进程类的一个对象用于和服务端保持连接,接收服务端回复的消息
 */
public class ClientConnectServerThread extends Thread {  //继承Thread 类
    private Socket socket;  //进程需要与socket相关联,所以Socket对象作为属性

    //构造器
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }  //由于服务端只有一个,所以只用传入Socket对象

    public Socket getSocket() {
        return socket;
    }  //通过线程对应的Socket对象

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {  //线程处于run 的状态,可以发送或接收消息
        while (true) {  //线程启动后,执行run(),需要持续等待读取服务端的消息,使用while
            try {
                System.out.println("客户端线程已启动,等待读取服务端回复的消息....");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  //线程执行run()期间需要不断接收服务端传来的Message对象/FileMessage对象

                //此时客户端已经连接成功,创建Message与FileMessage对象
                Message msg2;
                FileMessage fileMsg = new FileMessage();

                //注意msg2表示服务端回复的消息(有可能是服务端自己创建的,有可能是服务端转发的原消息); 注意只是Message对象,不是FileMessage对象
                msg2 = (Message) ois.readObject();  //注意将捕获的异常提升为Exception; 由于传来的对象已知是Massage类型,直接改为Message类型,并强制转换
                //由于要区分Message对象和FileMessage对象,写在外面会阻塞fileMsg的读取,所以读取过程写到if语句中

                //根据服务端回复的不同类型的Message对象,进行相应的业务处理
                if (msg2.getMsgType().equals(MessageType.MESSAGE_RETURN_ONLINE_FRIEND)) {  //客户端收到服务端的在线用户列表
                    //将从服务端传来的Message对象的内容(即在线用户列表),分割后存入String类型的数组,然后遍历数组将其按行显示出来
                    String onlineFriend[] = msg2.getContent().split(" ");
                    System.out.print("收到服务端传来的在线好友列表: ");
                    for(String string : onlineFriend){
                        System.out.print(string + " ");
                    }
                    System.out.println();

                    String userId = msg2.getGetter();  //请求在线用户列表的用户Id,也即服务端回复消息的接收者
                    FriendsListView friendsListView = ManageFriendsListView.getFriendsList(userId);  //根据 userId 得到对应的好友列表
                    //根据msg2更新在线好友列表; 注意要判断非空,当不需要更新时会传入一个null
                    if(friendsListView != null){
                        friendsListView.updateFriendsList(msg2);
                    }
                } else if (msg2.getMsgType().equals(MessageType.MESSAGE_PRIVATE_MSG)) {  //客户端B收到服务端转发的私聊消息
                    //把从服务端收到的消息,显示到相应的聊天界面中
                    ChatView chatView = ManageChatView.getChatView(msg2.getGetter() + msg2.getSender());  //得到对应的聊天界面(friendId + userId 对应私聊的好友的聊天窗口)
                    chatView.showMessage(msg2);  //调用聊天界面的方法在聊天界面显示消息
                    System.out.println("消息已经发到相应聊天界面: 用户 " + msg2.getSender() + " 对用户 " + msg2.getGetter() + " 说: " + msg2.getContent());
                } else if (msg2.getMsgType().equals(MessageType.MESSAGE_GROUP_MSG)) {  //各个客户端收到服务端转发的群聊消息
                    //把从服务端收到的消息,显示到相应的聊天界面中
                    ChatView chatView = ManageChatView.getChatView(msg2.getGetter() + msg2.getSender());  //得到对应的聊天界面(friendId + userId 对应群聊的好友的聊天窗口)
                    chatView.showMessage(msg2);  //调用聊天界面的方法在聊天界面显示消息
                    System.out.println("msg2的发送者为: " + msg2.getSender() + ",接收者为: " + msg2.getGetter());
                    System.out.println("消息已经发到相应聊天界面: 用户 " + msg2.getSender() + " 对大家说: " + msg2.getContent());
                } else if(msg2.getMsgType().equals(MessageType.MESSAGE_OFFLINE_MSG)) {  //客户端B离线
                    ChatView chatView = ManageChatView.getChatView(msg2.getSender() + msg2.getGetter());  //得到当前用户发送给离线用户的聊天界面
                    chatView.remindOffline();  //提示用户对方不在线
                } else if (fileMsg.getMsgType().equals(MessageType.MESSAGE_FILE_MSG)) {  //客户端B收到服务端转发的文件消息
//                    //阻塞读取文件消息对象fileMsg
//                    fileMsg = (FileMessage) ois.readObject();  //强制转换
                    //将文件消息的字节数组写出到磁盘中的接收路径
                    FileOutputStream fileOutputStream = new FileOutputStream(fileMsg.getDesFilePath());
                    fileOutputStream.write(fileMsg.getFileBytes());
                    System.out.println(fileMsg.getGetter() + " 已收到来自 " + fileMsg.getSrcFilePath() + " 的文件" + " , " + "文件已保存到 " + fileMsg.getDesFilePath());
                }
                else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
