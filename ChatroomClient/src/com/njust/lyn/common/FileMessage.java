package com.njust.lyn.common;

import java.io.Serializable;

/**
 * @author lyn
 * @date 2022/9/3 22:14
 * 客户端与服务端共有的文件消息对象，用于发送文件
 */
public class FileMessage implements Serializable {  //为了进行序列化，需要实现Serializable 接口
    private static final long serialVersionUID = 1L;  //为了提高序列化的兼容性
    private String sender;  //发送者
    private String getter;  //接收者
    private String sendTime;  //发送时间
    private String msgType;  //消息类型
    private byte[] fileBytes;  //文件字节数组
    private int fileLen;  //文件大小
    private String srcFilePath;  //发送路径
    private String desFilePath;  //接收路径

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

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getSrcFilePath() {
        return srcFilePath;
    }

    public void setSrcFilePath(String srcFilePath) {
        this.srcFilePath = srcFilePath;
    }

    public String getDesFilePath() {
        return desFilePath;
    }

    public void setDesFilePath(String desFilePath) {
        this.desFilePath = desFilePath;
    }
}
