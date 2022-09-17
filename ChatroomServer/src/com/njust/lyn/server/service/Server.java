package com.njust.lyn.server.service;

import com.njust.lyn.common.Message;
import com.njust.lyn.common.MessageType;
import com.njust.lyn.common.User;
import com.njust.lyn.common.UserType;
import com.njust.lyn.server.dao.LoginDAO;
import com.njust.lyn.server.dao.RegisterDAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author lyn
 * @date 2022/9/2 19:51
 * 服务端,用于监听9999端口
 * 将收到的客户端传来的User对象的用户名和密码进行验证
 * 向客户端发送具有特定msgType的Message对象
 *
 * 出现的bug1:向客户端回复Message对象时,使用new的Message对象可以运行,而使用作为属性的Message对象则产生大量异常
 * 原因:没有new作为属性的对象,也就是写成了private Message message;
 *
 * 出现的bug2:验证成功后服务端产生大量异常
 * 原因:客户端的UserClientService进行验证的逻辑处理时把MessageType接口的MESSAGE_LOGIN_SUCCEED字符串写成了"MESSAGE_LOGIN_SUCCEED"常量,就会导致判断为登录失败,然后关闭客户端的socket,由于服务端一直在监听,导致产生大量异常
 */
public class Server {
    private ServerSocket serverSocket = null;  //由于在之后需要用到User对象,所以User对象作为属性
//    private Message message = new Message();  //由于在之后需要用到Message对象,所以Message对象作为属性
//    private static HashMap<String, User> validUsers = new HashMap<>();  //集合对象作为属性,设为static,否则在静态代码块中无法直接使用validUsers 的put() 方法

    //用HashMap来模拟数据库
    // 静态代码块初始化用户名和密码,此为合法的用户列表
//    static {
//        validUsers.put("1", new User("1", "123"));
//        validUsers.put("2", new User("2", "123"));
//        validUsers.put("3", new User("3", "123"));
//        validUsers.put("4", new User("3", "123"));
//        validUsers.put("5", new User("3", "123"));
//        validUsers.put("6", new User("3", "123"));
//        validUsers.put("7", new User("3", "123"));
//        validUsers.put("8", new User("3", "123"));
//        validUsers.put("9", new User("3", "123"));
//        validUsers.put("10", new User("3", "123"));
//    }

    //通过将客户端发来的User对象的用户Id和密码与合法的用户列表相比较,验证登录的用户是否合法
    private boolean checkUserLogin(String userId, String password) {
        //使用 LoginDAO 操作数据库进行用户查询验证
        LoginDAO loginDAO = new LoginDAO();
        if (loginDAO.LoginCheck(userId, password)){
            return true;
        }else {
            return false;
        }

//        //采用"过关斩将"的方式进行验证
//        User user = validUsers.get(userId);  //通过要验证的userId 获取合法用户列表里的User对象
//        //先看用户id是否存在
//        if (user == null) {  //如果根据userId 要找的User 对象在集合中找不到,则user为空
//            return false;
//        }
//        else if (!user.getPassword().equals(password)){  //如果找到了相应的User对象,但密码不相同; 注意前面的!
//            return false;
//        }
//           return true;
    }

//    private boolean checkUser(String userId, String password) {
//        User user = validUsers.get(userId);
//        if (user != null && user.getPassword().equals(password)){  //如果找得到User对象,且密码一致
//            return true;
//        }else {
//            return false;
//        }
//    }

    //通过将客户端发来的User对象的用户Id与合法的用户列表相比较,验证注册的用户是否合法
    private boolean checkUserRegister(String userId, String password) {
        //使用 RegisterDAO 操作数据库进行用户查询验证
        RegisterDAO registerDAO = new RegisterDAO();
        if (registerDAO.registerCheck(userId)){  //如果验证成功(用户Id不重复)
            registerDAO.registerUser(userId, password);  //注册该用户
            return true;
        }else {
            return false;
        }
    }

    public Server() {  //无参构造器
        try {
            System.out.println("服务端对9999端口进行监听....");
            ServerSocket serverSocket = new ServerSocket(9999);  //服务端对9999端口进行监听

            while (true) {  //注意当服务端与客户端连接后,仍需持续监听
                Socket socket = serverSocket.accept();  //阻塞监听,直到客户端连接,返回Socket对象
                //连接成功后收到客户端的User对象
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  //创建与socket相关联的对象输入流
                User user = (User) ois.readObject();  //注意将捕获的异常提升为Exception; 由于传来的对象已知是User类型,直接改为User类型,并强制转换
                //创建要发送给客户端Message对象
                Message message = new Message();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  //创建与socket相关联的对象输出流
                //判断客户端发来的User对象是要登录还是注册
                if(user.getUserType().equals(UserType.USER_LOGIN)){  //如果是登录
                    //根据收到的User对象的用户Id与密码,进行验证
                    if (checkUserLogin(user.getUserId(), user.getPassword())) {  //验证登录成功
                        message.setMsgType(MessageType.MESSAGE_LOGIN_SUCCEED);  //更改Message对象的msgType属性
                        //向客户端发送Message对象
                        oos.writeObject(message);  //将User对象送到与socket相关联的输出流上,发送Message对象
                        //创建一个和客户端持续通信的线程,即ServerConnectClientThread 类
                        ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(user.getUserId(), socket);  //得到服务端与socket相关联的线程对象
                        serverConnectClientThread.start();  //启动线程,线程启动后会执行 run()
                        //为了客户端之后的扩展性,将线程放入集合中方便管理
                        ManageServerConnectClient.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);
                        //用户登录成功后,通过其对应的线程通知其他在线用户的进程,说明该用户已上线
                        ServerConnectClientThread.notifyOtherUsers(user.getUserId());
                    } else {  //验证登录失败
                        message.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);  //更改Message对象的msgType属性
                        //向客户端发送Message对象
                        oos.writeObject(message);  //将User对象送到与socket相关联的输出流上,发送Message对象
                        socket.close();  //登录失败后,将socket关闭,下次循环仍会创建
                    }
                }else if(user.getUserType().equals(UserType.USER_REGISTER)){  //如果是注册
                    //根据收到的User对象的用户Id,进行验证
                    if (checkUserRegister(user.getUserId(), user.getPassword())) {  //验证注册成功
                        message.setMsgType(MessageType.MESSAGE_REGISTER_SUCCEED);  //更改Message对象的msgType属性
                        //向客户端发送Message对象
                        oos.writeObject(message);  //将User对象送到与socket相关联的输出流上,发送Message对象
                    }else {  //验证注册失败
                        message.setMsgType(MessageType.MESSAGE_REGISTER_FAIL);  //更改Message对象的msgType属性
                        //向客户端发送Message对象
                        oos.writeObject(message);  //将User对象送到与socket相关联的输出流上,发送Message对象
                        socket.close();  //注册失败后,将socket关闭,下次循环仍会创建
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //到这已经是退出while 了,表示不再监听, 关闭serverSocket
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
