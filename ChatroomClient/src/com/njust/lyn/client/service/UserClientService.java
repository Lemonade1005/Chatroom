package com.njust.lyn.client.service;

import com.njust.lyn.client.utils.Utility;
import com.njust.lyn.common.Message;
import com.njust.lyn.common.MessageType;
import com.njust.lyn.common.User;
import com.njust.lyn.common.UserType;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author lyn
 * @date 2022/9/2 13:17
 * 向服务端发送请求,完成一些客户端的功能:登录、注册
 */
public class UserClientService {
    private User user = new User();  //由于在之后需要用到User对象,所以User对象作为属性
    private Socket socket;  //由于在之后需要用到Socket对象,所以Socket对象作为属性; 注意这里没有实例化,因为不同方法会有不同的Socket对象(?)

    //到服务端验证用户名和密码,返回一个boolean类型的值,表示成功与否
    public boolean checkUser(String userId, String pwd) {  //需要把userId和pwd设置给User对象,传给服务端
        boolean check = false;  //默认false
        //向服务端发送的User对象的属性
        this.user.setUserId(userId);
        this.user.setPassword(pwd);
        this.user.setUserType(UserType.USER_LOGIN);

        try {
            //连接服务端
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);  //使用socket连接,服务端IP:127.0.0.1, 端口:9999
            //向服务端发送User对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  //创建与socket相关联的对象输出流
            oos.writeObject(user);  //将User对象送到与socket相关联的输出流上,发送User对象

            //从服务端接收Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  //创建与socket相关联的对象输入流
            //阻塞读取
            Message msg = (Message) ois.readObject();  //注意将捕获的异常提升为Exception; 由于传来的对象已知是Massage类型,直接改为Message类型,并强制转换

            //根据服务端传回的Message对象的msgType属性,做出相应的逻辑处理
            if (msg.getMsgType().equals(MessageType.MESSAGE_LOGIN_SUCCEED))  //登录成功
            {
                //用户名与密码核验成功后,创建一个和服务端持续通信的线程,即ClientConnectServerThread 类
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);  //得到客户端与socket相关联的线程对象
                clientConnectServerThread.start();  //启动线程,线程启动后会执行 run()
                //为了客户端之后的扩展性,将线程放入集合中方便管理
                ManageClientConnectServer.addClientConnectServerThread(userId, clientConnectServerThread);
                //更改check值
                check = true;
            } else if(msg.getMsgType().equals(MessageType.MESSAGE_LOGIN_FAIL)){  //登录失败
                socket.close();  //不启动线程,直接关闭socket
                check = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    //到服务端注册账号,返回一个boolean类型的值,表示成功与否
    public boolean RegisterUser(String userId, String pwd) {
        boolean check = false;  //默认false
        //向服务端发送的User对象的属性
        this.user.setUserId(userId);
        this.user.setPassword(pwd);
        this.user.setUserType(UserType.USER_REGISTER);

        try {
            //连接服务端
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);  //使用socket连接,服务端IP:127.0.0.1, 端口:9999
            //向服务端发送User对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  //创建与socket相关联的对象输出流
            oos.writeObject(user);  //将User对象送到与socket相关联的输出流上,发送User对象

            //从服务端接收Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  //创建与socket相关联的对象输入流
            //阻塞读取
            Message msg = (Message) ois.readObject();  //注意将捕获的异常提升为Exception; 由于传来的对象已知是Massage类型,直接改为Message类型,并强制转换

            //根据服务端传回的Message对象的msgType属性,做出相应的逻辑处理
            if (msg.getMsgType().equals(MessageType.MESSAGE_REGISTER_SUCCEED))  //注册成功
            {
                //更改check值
                check = true;
            } else if(msg.getMsgType().equals(MessageType.MESSAGE_REGISTER_FAIL)){  //注册失败
                //提示用户注册失败,重新注册

                socket.close();  //直接关闭socket
                check = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }


    //将客户端获取在线用户列表的请求发送给服务端
    public void onlineUser() {  //注意这里没有返回值,而服务端的getOnlineUser()有String类型的返回值,即相应的字符串由服务端处理好再发送过来
        //创建一个Message对象
        Message msg = new Message();
        //设置消息类型,表示请求获取在线用户列表
        msg.setMsgType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        //设置发送者,在服务端会根据此消息msg的消息发送者,设置回复消息msg2的接收者
        msg.setSender(user.getUserId());

        try {
            //从线程集合中,通过userId,得到已启动的线程对象
            ClientConnectServerThread clientConnectServerThread =
                    ManageClientConnectServer.getClientConnectServerThread(user.getUserId());
            //通过线程得到对应的Socket对象
            socket = clientConnectServerThread.getSocket();
            //将Message对象发送给服务端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //

    //将客户端结束进程的请求发送给服务端
    public void logout() {  //没有返回值
        //创建一个Message对象
        Message msg = new Message();
        //设置消息类型,表示请求结束进程
        msg.setMsgType(MessageType.MESSAGE_CLIENT_EXIT);
        //设置发送者,在服务端会根据此消息msg的消息发送者,设置回复消息msg2的接收者
        msg.setSender(user.getUserId());

        try {
            //从线程集合中,通过userId,得到已启动的线程对象
            ClientConnectServerThread clientConnectServerThread =
                    ManageClientConnectServer.getClientConnectServerThread(user.getUserId());
            //通过线程得到对应的Socket对象
            socket = clientConnectServerThread.getSocket();
            //将Message对象发送给服务端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //注意当一个客户端中存在多个线程时,建议写成如下的形式
            //解读:通过userId从管理线程的集合中得到相应进程,通过该进程得到与之相关联的Socket对象,再通过Socket对象创建对象输出流
//            ObjectOutputStream oos =
//                    new ObjectOutputStream(ManageClientConnectServer.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(msg);
            System.out.println("退出成功");
            System.exit(0);  //结束客户端进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
