package com.njust.lyn.view;

import com.njust.lyn.server.service.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author lyn
 * @date 2022/9/7 14:15
 * 展示服务端界面
 */
public class ServerView extends JFrame implements MouseListener {
    JButton jb1, jb2;  //开启服务器按钮,关闭服务器按钮
    JPanel jp1;
    JFrame jf;

    public static void main(String[] args) {
//        ServerView serverView = new ServerView();
    }

    public ServerView(){
        jb1 = new JButton("开启服务器");
        jb1.addMouseListener(this);  //监听服务器
        jb2 = new JButton("关闭服务器");
        jb2.addMouseListener(this);  //监听服务器
        jp1 = new JPanel();
        jp1.add(jb1);
        jp1.add(jb2);

        //设置字体
        Font font = new Font("宋体", 20, 30);
        jb1.setFont(font);
        jb2.setFont(font);

        //设置窗口
        jf = new JFrame("服务端");
        jf.setSize(800, 600);  //窗口大小
        jf.setLocation(605, 300);  //窗口位置
        jf.add(jp1);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == jb1){  //如果点击开启服务器按钮
            new Server();
        }else if(e.getSource() == jb2){  //如果点击关闭服务器按钮

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    //设置鼠标在按钮上的样式
    @Override
    public void mouseEntered(MouseEvent e) {
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        if(e.getSource() == jb1) {
            jf.setCursor(handCursor);
        }else if(e.getSource() == jb2){
            jf.setCursor(handCursor);
        }
    }

    //设置鼠标离开按钮的样式
    @Override
    public void mouseExited(MouseEvent e) {
        Cursor nwResizeCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        if(e.getSource() == jb1){
            jf.setCursor(nwResizeCursor);
        }else if(e.getSource() == jb2){
            jf.setCursor(nwResizeCursor);
        }
    }
}
