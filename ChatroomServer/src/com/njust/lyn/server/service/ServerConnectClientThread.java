package com.njust.lyn.server.service;

import com.njust.lyn.common.FileMessage;
import com.njust.lyn.common.Message;
import com.njust.lyn.common.MessageType;
import com.njust.lyn.common.User;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author lyn
 * @date 2022/9/2 21:09
 * 该进程类的一个对象用于和客户端保持连接
 */
public class ServerConnectClientThread extends Thread {  //继承Thread 类
    private Socket socket;  //进程需要与socket相关联,所以Socket对象作为属性
    private String userId;  //连接到服务端的用户id

    //构造器
    public ServerConnectClientThread(String userId, Socket socket) {  //由于客户端可以有很多个,所以还需要传入连接的用户id
        this.userId = userId;
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    //通知其他线程,本线程对应的用户已上线
    public static void notifyOtherUsers(String userId) {  //参数: 新上线用户Id
        HashMap hm = ManageServerConnectClient.getHm();  //直接得到存放所有在线用户进程的集合
        //使用迭代器遍历
        Iterator iterator = hm.keySet().iterator();
        while (iterator.hasNext()) {
            String onlineUserId = iterator.next().toString();  //其他在线用户的Id
            Message msg2 = new Message();
            msg2.setGetter(onlineUserId);  //接收者为其他在线用户
            msg2.setContent(userId);  //消息内容为新上线的用户Id
            msg2.setMsgType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);  //消息类型为回复获取在线用户的请求
            try {
                ObjectOutputStream oos =
                        new ObjectOutputStream(ManageServerConnectClient.getServerConnectClientThread(onlineUserId).getSocket().getOutputStream());
                oos.writeObject(msg2);  //将消息回复给客户端
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {  //线程处于run 的状态,可以发送或接收消息; 注意这里处理的是客户端验证成功后发的消息
        boolean isLine = true;  //表示用户是否在线,用户退出后置为false,用于解决用户退出后产生SocketException的问题
        ObjectInputStream ois = null;  //为了最后关闭,提升了作用域
        ObjectOutputStream oos = null;
        Message msg = null;
        while (isLine) {  //线程启动后,执行run(),需要持续等待读取客户端的消息,使用while
            try {
                System.out.println("用户 " + userId + " 已连接,等待读取数据....");
                socket = ManageServerConnectClient.getServerConnectClientThread(userId).getSocket();  //得到已连接的客户端Socket对象
                ois = new ObjectInputStream(socket.getInputStream());  //线程执行run()期间需要不断接收客户端传来的Message对象/FileMessage对象
//                FileInputStream fileInputStream = null;
//                //此时客户端已经连接成功,阻塞读取客户端的Message对象
                msg = (Message) ois.readObject();

                //此时客户端已经连接成功,创建Message和FileMessage对象; 注意由于对象不同,并且读取时是阻塞读取,所以读取过程写入if语句中
//                Message msg = new Message();
                FileMessage fileMsg = new FileMessage();  //为什么这里冗余了???
                //根据用户在键盘上的输入,收到客户端发来的不同类型的Message对象,进行相应的业务处理
                if (msg.getMsgType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {  //客户端请求获得在线用户列表
                    //分析:客户端刚登录成功,服务端就把该线程加入到集合中只需根据集合中的线程即可知道在线用户。
                    //用户登录成功后,服务端将消息类型设置为 MESSAGE_RETURN_ONLINE_FRIEND,消息内容设置为在线用户列表,消息接收者设置为发送请求的用户Id
                    System.out.println("用户 " + userId + " 请求获取在线用户列表");
                    //创建要发送给客户端的Message对象
                    Message msg2 = new Message();
                    //在线用户列表
                    String onlineUserList = ManageServerConnectClient.getOnlineUserList();
                    String[] onlineUserArray = ManageServerConnectClient.getOnlineUserArray();
                    //设置回复消息msg2的属性
                    msg2.setMsgType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);  //设置类型为服务端回复在线用户列表
                    msg2.setContent(onlineUserList);  //设置内容为处理好的在线用户列表
                    msg2.setGetter(msg.getSender());  //根据客户端发来的消息msg的消息发送者,设置回复消息msg2的接收者
                    //将Message对象回复给客户端
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(msg2);
//                    //服务端向客户端发送消息,通知各个客户端更新在线好友列表
//                    for(int i = 0; i < ManageServerConnectClient.getHm().size(); i++) {
//                        msg2.setMsgType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);  //设置类型为服务端回复在线用户列表
//                        msg2.setContent(onlineUserList);  //设置内容为处理好的在线用户列表
//
//                        msg2.setGetter(onlineUserArray[i]);  //设置回复消息msg2的接收者为当前在线的所有用户;注意使用的是获取在线用户Id数组的方法
//                        socket = ManageServerConnectClient.getServerConnectClientThread(msg2.getGetter()).getSocket();  //得到要发送更新消息的客户端Socket对象
//                        try {
//                            oos = new ObjectOutputStream(socket.getOutputStream());  //得到要发送更新消息的客户端Socket对象对应的输出流
//                            oos.writeObject(msg2);
//                        } catch (IOException ioException) {
//                            ioException.printStackTrace();
//                        }
//                    }
                    System.out.print("新用户登录,通知其他用户,服务端处理好的在线用户列表: " + msg2.getContent());
                    System.out.println();
                } else if (msg.getMsgType().equals(MessageType.MESSAGE_PRIVATE_MSG)) {  //客户端发送的私人聊天消息
                    System.out.println("服务端收到私聊: 用户 " + msg.getSender() + " 对用户 " + msg.getGetter() + " 说: " + msg.getContent());
                    //改变消息发送所使用的Socket对象,即把消息从客户端A的进程发送改成从客户端B的进程发送(因为只有服务端才管理了多个客户端的进程),转发客户端的消息msg
                    ServerConnectClientThread serverConnectClientThread = ManageServerConnectClient.getServerConnectClientThread(msg.getGetter());  //注意要使用接收者的userId获取相应的进程,否则是自己跟自己通信
                    //判断客户端B是否处于离线状态
                    if(serverConnectClientThread == null){  //如果客户端B处于离线状态,服务端找到的线程为null,则将提示消息原路返回,提示客户端A无法聊天
                        Message msg2 = new Message();
                        msg2.setSender(msg.getSender());
                        msg2.setGetter(msg.getGetter());
                        msg2.setMsgType(MessageType.MESSAGE_OFFLINE_MSG);
                        Socket socket = ManageServerConnectClient.getServerConnectClientThread(msg.getSender()).getSocket();  //客户端A的Socket对象
                        oos = new ObjectOutputStream(socket.getOutputStream());  //客户端A的Socket对象对应的输出流
                        oos.writeObject(msg2);
                        System.out.println("对方离线");
                    }else {  //如果客户端B处于在线状态,正常转发聊天消息
                        Socket socket2 = serverConnectClientThread.getSocket();  //客户端B的Socket对象
                        oos = new ObjectOutputStream(socket2.getOutputStream());  //客户端B的Socket对象对应的输出流
                        oos.writeObject(msg);  //转发客户端的消息发来的msg
                        System.out.println("服务端发回: 用户 " + msg.getSender() + " 对用户 " + msg.getGetter() + " 说: " + msg.getContent() + " ");
                    }
                } else if (msg.getMsgType().equals(MessageType.MESSAGE_GROUP_MSG)) {  //客户端发送的群组聊天消息
                    System.out.println("服务端收到群聊: 用户 " + msg.getSender() + " 对用户 " + msg.getGetter() + " 说: " + msg.getContent() + " (群聊没有指定接收者)");
                    Message msg2 = new Message();

                    //bug: 给不在线用户群发时没有不在线的提示
//                    ServerConnectClientThread serverConnectClientThread = ManageServerConnectClient.getServerConnectClientThread(msg.getGetter());
//                    //判断客户端B是否处于离线状态
//                    if(serverConnectClientThread == null){  //如果客户端B处于离线状态,服务端找到的线程为null,则将提示消息原路返回,提示客户端A无法聊天


                    msg2.setMsgType(MessageType.MESSAGE_GROUP_MSG);
                    msg2.setSender(msg.getSender());
                    //遍历管理线程的集合,将Message对象转发给除自己之外的其他客户端; 注意群聊只用得到在线用户列表,群发消息即可,与私聊不同,不用判断是否在线
                    HashMap<String, ServerConnectClientThread> hm = ManageServerConnectClient.getHm();  //获得管理线程的集合
                    Iterator<String> iterator = hm.keySet().iterator();  //迭代器遍历集合的key(不太熟练)
                    //循环设置并回复设置好接收者的 msg2
                    while (iterator.hasNext()) {
                        //取出在线用户的id
                        String onlineId = iterator.next();
                        System.out.println("在线的用户有: " + onlineId + " ");
                        //发给除了发送者以外的在线用户
                        if (!onlineId.equals(msg.getSender())) {  //注意 msg.getSender() 是群发消息的客户端
                            msg2.setSender(msg.getSender());
                            msg2.setGetter(onlineId);  //服务端设置好了群聊消息的接收者,客户端直接在对应的窗口显示即可
                            msg2.setContent(msg.getContent());
                            System.out.println("msg2的接收者为: " + msg2.getGetter());
                            oos = new ObjectOutputStream(hm.get(onlineId).getSocket().getOutputStream());  //直接用集合hm获得相应的线程
                            oos.writeObject(msg2);
                        }
                    }
                } else if (msg.getMsgType().equals(MessageType.MESSAGE_FILE_MSG)) {  //客户端请求发送文件
//                    //阻塞读取文件消息对象fileMsg,注意不是msg
//                    fileMsg = (FileMessage) ois.readObject();  //强制转换
                    //根据文件消息的接收者转发给相应的客户端
                    oos = new ObjectOutputStream(ManageServerConnectClient.getServerConnectClientThread(fileMsg.getGetter()).getSocket().getOutputStream());  //这段代码与上面私聊的部分是一样的,只是更简洁了
                    oos.writeObject(fileMsg);  //转发客户端的文件消息msg
                } else if (msg.getMsgType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {  //客户端请求结束进程
                    //从服务端管理线程的集合中移除该进程
                    ManageServerConnectClient.removeServerConnectClientThread(userId, ManageServerConnectClient.getServerConnectClientThread(userId));
                    //关闭当前进程的Socket对象; 注意服务端有很多进程,各自持有一个Socket对象
                    socket.close();
                    System.out.println("用户 " + userId + " 已退出客户端");
                    break;  //退出循环,不再持续监听,run() 方法结束,进程结束
                } else {  //其他消息的类型,暂时不处理
                    System.out.println("其他消息的类型,暂时不处理");
                }

            } catch (SocketException e) {
                isLine = false;
                System.out.println("用户 " + userId + " 下线了");
                ServerConnectClientThread serverConnectClientThread = ManageServerConnectClient.getServerConnectClientThread(userId);
                ManageServerConnectClient.removeServerConnectClientThread(userId, serverConnectClientThread);  //将下线的用户的线程从集合中移除
                String onlineUserList = ManageServerConnectClient.getOnlineUserList();


//                //当有用户退出时,服务端发送一个在线好友的消息给客户端,提示客户端更新在线好友
//                //此处需要从连接的线程的集合中移除此用户对应的线程
//                ServerConnectClientThread serverConnectClientThread = ManageServerConnectClient.getServerConnectClientThread(userId);  //根据userId得到线程
//                ManageServerConnectClient.removeServerConnectClientThread(userId, serverConnectClientThread);  //根据userId和对应的线程从集合中移除这个线程
//                Message msg2 = new Message();
//                for(int i = 0; i < ManageServerConnectClient.getHm().size(); i++) {  //将更新好友列表的消息发给每个在线的用户
//                    msg2.setMsgType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);  //设置类型为服务端回复在线用户列表
//                    msg2.setContent(ManageServerConnectClient.getOnlineUserList());  //设置内容为处理好的在线用户列表
//                    System.out.print("服务端处理好的在线用户列表: " + msg2.getContent());
//                    System.out.println();
//                    msg2.setGetter(ManageServerConnectClient.getOnlineUserArray()[i]);  //设置回复消息msg2的接收者为当前在线的所有用户;注意使用的是获取在线用户Id数组的方法
//                    socket = ManageServerConnectClient.getServerConnectClientThread(msg2.getGetter()).getSocket();  //得到要发送更新消息的客户端Socket对象
//                    try {
//                        oos = new ObjectOutputStream(socket.getOutputStream());  //得到要发送更新消息的客户端Socket对象对应的输出流
//                        oos.writeObject(msg2);
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//                }
//                System.out.println("当前在线用户有: " + msg2.getContent());

//                System.out.println("当前在线用户有: " + onlineUserList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //用户下线后关闭资源
                if(!isLine && ois != null) {  //用户下线后并且输入流不为空才关闭; 注意要是不判断用户下线就关闭流会导致EOFException
                    try {
                        ois.close();  //输入流不为空时,输出流一定也不为空,所以一起关了
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
