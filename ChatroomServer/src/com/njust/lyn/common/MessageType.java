package com.njust.lyn.common;

/**
 * @author lyn
 * @date 2022/9/2 12:05
 * 消息类型接口，用于服务端验证用户后发送提示消息
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1";  //登录成功
    String MESSAGE_LOGIN_FAIL = "2";  //登录失败
    String MESSAGE_REGISTER_SUCCEED = "3";  //注册成功
    String MESSAGE_REGISTER_FAIL = "4";  //注册失败
    String MESSAGE_GET_ONLINE_FRIEND = "5";  //客户端请求获取在线用户列表
    String MESSAGE_RETURN_ONLINE_FRIEND = "6";  //服务端返回在线用户列表
    String MESSAGE_PRIVATE_MSG = "7";  //私人的聊天消息
    String MESSAGE_GROUP_MSG = "8";  //群组的聊天消息
    String MESSAGE_OFFLINE_MSG = "9";  //服务端回复客户端B离线
    String MESSAGE_FILE_MSG = "10";  //客户端的文件消息
    String MESSAGE_CLIENT_EXIT = "11";  //客户端请求退出
}
