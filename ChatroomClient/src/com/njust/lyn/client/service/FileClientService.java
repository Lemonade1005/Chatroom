package com.njust.lyn.client.service;

import com.njust.lyn.client.utils.Utility;
import com.njust.lyn.common.FileMessage;
import com.njust.lyn.common.MessageType;
import com.njust.lyn.common.User;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * @author lyn
 * @date 2022/9/3 22:10
 * 该类的一个对象用于提供发送文件的服务
 */
public class FileClientService {
    private User user = new User();
    private Socket socket = new Socket();

    public void sendFile(String senderId) {  //发送文件
        System.out.print("请输入对方的用户名: ");
        String getterId = Utility.readString(10);  //用户输入聊天对象,长度不超过10
        System.out.print("请输入文件发送路径: ");
        String srcFilePath = Utility.readString(100);  //用户输入文件发送路径,长度不超过100
        System.out.print("请输入文件存放路径: ");
        String desFilePath = Utility.readString(100);  //用户输入文件存放路径,长度不超过100

        //创建一个FileMessage对象
        FileMessage fileMsg = new FileMessage();
        //设置FileMessage对象的属性
        fileMsg.setSender(senderId);  //文件消息发送者
        fileMsg.setGetter(getterId);  //文件消息接收者
        fileMsg.setMsgType(MessageType.MESSAGE_FILE_MSG);  //文件消息类型
        fileMsg.setSrcFilePath(srcFilePath);  //文件消息发送路径
        fileMsg.setDesFilePath(desFilePath);  //文件消息接收路径
        fileMsg.setSendTime(new Date().toString());  //文件发送日期

        //创建文件字节数组,存放读到的文件字节
        byte[] fileBytes = new byte[(int) new File(srcFilePath).length()];  //长度与要发送的文件一致,将long强制转换为int

        //文件输入流
        FileInputStream fileInputStream = null;  //由于要在finally中关闭流,所以提升作用域

        try {
            //从磁盘中读入要发送的文件(按字节)
            fileInputStream = new FileInputStream(srcFilePath);
            fileInputStream.read(fileBytes);  //将字节输入流中的数据读到字节数组fileBytes中
            //将fileBytes中的数据设置为文件消息对象fileMsg的字节数组属性中
            fileMsg.setFileBytes(fileBytes);

            //将fileMessage对象发送给服务端,注意不是msg
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServer.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(fileMsg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
