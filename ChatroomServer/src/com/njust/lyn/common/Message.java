package com.njust.lyn.common;

import java.io.Serializable;

/**
 * @author lyn
 * @date 2022/9/2 11:51
 * 客户端与服务端共有的消息对象，用于通信
 */
public class Message implements Serializable {  //为了进行序列化，需要实现Serializable 接口
    private static final long serialVersionUID = 1L;  //为了提高序列化的兼容性
    private String sender;  //发送者
    private String getter;  //接收者
    private String content;  //发送内容
    private String sendTime;  //发送时间
    private String msgType;  //消息类型

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
