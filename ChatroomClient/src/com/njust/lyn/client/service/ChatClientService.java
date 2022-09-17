package com.njust.lyn.client.service;

import com.njust.lyn.client.utils.Utility;
import com.njust.lyn.common.Message;
import com.njust.lyn.common.MessageType;
import com.njust.lyn.common.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author lyn
 * @date 2022/9/3 15:08
 * 该类的一个对象提供与聊天消息有关的方法
 */
public class ChatClientService {
    private User user = new User();  //由于在之后需要用到User对象,所以User对象作为属性
    private Socket socket = new Socket();  //由于在之后需要用到Socket对象,所以Socket对象作为属性; 注意这里没有实例化,因为不同方法会有不同的Socket对象(?) (尝试一下实例化)

    //将客户端私聊的消息发送给服务端
    public void privateChat(String senderId, String getterId, String content) {  //没有返回值
        //创建一个Message对象
        Message msg = new Message();
        //设置消息对象的属性
        msg.setMsgType(MessageType.MESSAGE_PRIVATE_MSG);  //设置消息类型,表示私人聊天消息
        msg.setContent(content);  //设置消息内容
        msg.setSendTime(new Date().toString());  //设置消息发送时间
        msg.setSender(senderId);  //设置发送者,在服务端会根据此消息msg的消息发送者,设置回复消息msg2的getterId
        msg.setGetter(getterId);  //设置接收者
        System.out.println("执行服务类方法: 用户 " + msg.getSender() + " 对用户 " + msg.getGetter() + " 说: " + msg.getContent());
        try {
            //从线程集合中,通过userId,得到已启动的线程对象
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServer.getClientConnectServerThread(senderId);
            //通过线程得到对应的Socket对象
            socket = clientConnectServerThread.getSocket();
            //将Message对象发送给服务端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msg);
            System.out.println("发给服务端时: 用户 " + msg.getSender() + " 对用户 " + msg.getGetter() + " 说: " + msg.getContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将客户端群聊的消息发送给服务端
    public void groupChat(String senderId, String content) {  //没有返回值
        //创建一个Message对象
        Message msg = new Message();
        //设置消息对象的属性
        msg.setMsgType(MessageType.MESSAGE_GROUP_MSG);  //设置消息类型,表示群组的聊天消息
        msg.setContent(content);  //设置消息内容
        msg.setSendTime(new Date().toString());  //设置消息发送时间
        msg.setSender(senderId);  //设置发送者,服务端会根据此消息msg,设置回复消息msg2,发送者仍为此用户Id,接收者设置为除此用户以外的其他在线用户
        //由于点击群发按钮每次只有一次,所以只会向服务端发送一次消息,所以消息的接收者需要到服务端用循环设置,然后再发回到客户端,客户端进行持续读取,根据服务端处理好的接收者，在相应的窗口显示出来

        try {
            //从线程集合中,通过userId,得到已启动的线程对象
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServer.getClientConnectServerThread(senderId);
            //通过线程得到对应的Socket对象
            socket = clientConnectServerThread.getSocket();
            //将Message对象发送给服务端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msg);
            System.out.println("\n" + "用户 " + msg.getSender() + " 对大家说: " + msg.getContent() + "  (" + msg.getSendTime() + ")");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
