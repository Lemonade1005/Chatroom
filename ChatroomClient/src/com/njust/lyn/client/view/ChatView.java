package com.njust.lyn.client.view;

import com.njust.lyn.client.service.ChatClientService;
import com.njust.lyn.client.service.ClientConnectServerThread;
import com.njust.lyn.client.service.UserClientService;
import com.njust.lyn.common.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author lyn
 * @date 2022/9/6 20:19
 * 展示用户聊天界面
 */
public class ChatView extends JFrame implements ActionListener, MouseListener, FocusListener {
    JTextArea textArea;
    JTextField textField;
    JButton jb, jb1;  //jb发送按钮、jb1群发按钮
    JScrollPane jsp;
    JPanel jp;
    JFrame jf;

    //监听事件中要用到userId和FriendId,将其作为属性
    String userId;
    String friendId;
    //进程需要与socket相关联,所以Socket对象作为属性
    Socket socket;

    public static void main(String[] args) {
        ChatView chatView = new ChatView("1", "2");  //参数:聊天好友Id
    }

    public ChatView(String userId, String friendId){  //参数:发送者Id,接收者Id
        this.userId = userId;  //为userId这个属性赋值,使其作为全局变量
        this.friendId = friendId;

        //中部
        textArea = new JTextArea();
        textArea.setEnabled(false);  //文本域禁用编辑
        jsp = new JScrollPane(textArea);
        //南部
        textField = new JTextField("请输入....", 40);
        //根据焦点实现文本框提示文字
        textField.addFocusListener(this);  //监听文本框
        jb = new JButton("发送");
        jb.addMouseListener(this);  //监听发送按钮
        jb1 = new JButton("群发");
        jb1.addMouseListener(this);  //监听群发按钮
//        jp = new JPanel(new GridLayout(1, 3));  //网格布局
        jp = new JPanel(new FlowLayout());  //流式布局
        jp.add(textField, "Center");
        jp.add(jb, "Center");
        jp.add(jb1, "Center");

        //设置字体
        Font font1 = new Font("宋体", 20, 20);
        Font font2 = new Font("宋体", Font.BOLD, 20);
        textField.setFont(font1);
        textField.setForeground(Color.gray);
        textArea.setFont(font2);
        textArea.setForeground(Color.black);
        jb.setFont(font1);
        jb1.setFont(font1);

        //设置颜色
        textArea.setBackground(Color.decode("#f5f7fa"));
        jp.setBackground(Color.decode("#c3cfe2"));

        //放入窗口
        jf = new JFrame(userId + " 正在和 " + friendId + " 聊天");
        jf.setSize(700, 550);  //窗口大小
        jf.setLocation(630, 300);  //窗口位置
        ImageIcon icon = new ImageIcon("resource/icon.png");  //图标
        jf.setIconImage(icon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));  //图标大小
        jf.add(jsp, "Center");
        jf.add(jp, "South");
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);  //聊天窗口关闭不影响其他程序,所以关闭方式为隐藏
    }

    //根据传入的 Message 对象,将内容显示在文本域中
    //在客户端 B 中显示聊天内容
    public void showMessage(Message msg){
        //得到格式化的 年-月-日  时-分-秒
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        textArea.append("用户 " + msg.getSender() + " 对用户 " + msg.getGetter() + " 说: " + msg.getContent() + " (" + dateFormat.format(date) + ")\n");
    }

    //在聊天界面提醒对方不在线
    public void remindOffline() {
        JOptionPane.showMessageDialog(this, "对方不在线!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == jb) {  //如果点击发送按钮
            String userId = this.userId;
            String friendId = this.friendId;
            String content = textField.getText();
            //提示发送内容不可为空
            if(content.equals("") || content.equals("请输入....")){  //暴力解决发出提示文字
                JOptionPane.showMessageDialog(this, "发送内容不可为空!");;
            }else {
                ChatClientService chatClientService = new ChatClientService();  //创建聊天服务对象
                chatClientService.privateChat(userId, friendId, content);  //调用聊天服务对象中的私聊方法
                textField.setText("");  //点击发送按钮后,清空输入框内容
                //得到格式化的 年-月-日  时-分-秒
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");  //注: hh表示12小时制; HH表示24小时制
                //在客户端 A 显示聊天内容
                textArea.append("用户 " + userId + " 对用户 " + friendId + " 说: " + content + " (" + dateFormat.format(date) + ")\n");
            }
        }
        if(e.getSource() == jb1) {  //如果点击群发按钮
            String userId = this.userId;
            String content = textField.getText();
            //提示发送内容不可为空
            if(content.equals("") || content.equals("请输入....")){  //暴力解决发出提示文字
                JOptionPane.showMessageDialog(this, "发送内容不可为空!");;
            }else {
                ChatClientService chatClientService = new ChatClientService();  //创建聊天服务对象
                chatClientService.groupChat(userId, content);  //调用聊天服务对象中的群聊方法
                textField.setText("");  //点击群发按钮后,清空输入框内容
                //得到格式化的 年-月-日  时-分-秒
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");  //注: hh表示12小时制; HH表示24小时制
                //在客户端 A 显示聊天内容
                textArea.append("用户 " + userId + " 对大家说: " + content + " (" + dateFormat.format(date) + ")\n");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    //设置鼠标在发送、群发按钮上的样式
    @Override
    public void mouseEntered(MouseEvent e) {
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        if(e.getSource() == jb){
            jf.setCursor(handCursor);
        }else if(e.getSource() == jb1){
            jf.setCursor(handCursor);
        }
    }

    //设置鼠标离开发送、群发按钮的样式
    @Override
    public void mouseExited(MouseEvent e) {
        Cursor nwResizeCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        if(e.getSource() == jb){
            jf.setCursor(nwResizeCursor);
        }else if (e.getSource() == jb1){
            jf.setCursor(nwResizeCursor);
        }
    }

    //设置文本框获得焦点时的样式
    @Override
    public void focusGained(FocusEvent e) {
        if(textField.getText().equals("请输入....")){
            textField.setText("");
            textField.setForeground(Color.black);
        }
    }

    //设置文本框失去焦点时的样式
    @Override
    public void focusLost(FocusEvent e) {
        if(textField.getText().equals("")){
            textField.setForeground(Color.gray);
            textField.setText("请输入....");
        }
    }

}
