package com.njust.lyn.client.view;

import com.njust.lyn.client.service.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * @author lyn
 * @date 2022/9/6 0:02
 * 展示用户登录界面
 */
public class LoginView extends JFrame implements ActionListener, MouseListener, KeyListener {
    //窗口大小555*420, 位置705*360
    //布局:北部一个JLabel,中部一个JPanel,南部一个JPanel
    JFrame jf;
    JLabel jl1, jp1_jl1, jp1_jl2, jp1_jl3, jp1_jl4;  //北部1个标签,中部4个标签
    JPanel jp1, jp2;  //中部和南部的面板
    JTextField userIdTextField;  //文本框
    JPasswordField passwordField;  //密码框
    JComboBox jcBox;  //下拉框
    JCheckBox jcBox1, jcBox2;  //复选框
    JButton jp2_jb1, jp2_jb2;  //按钮
    //为了直接使用UserClientService 类中的方法,将这个对象作为属性
    private UserClientService userClientService = new UserClientService();
//    private ChatClientService chatClientService = new ChatClientService();
//    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) {
        LoginView loginView = new LoginView();
    }

    public LoginView() {
        //北部
        jl1 = new JLabel(new ImageIcon("resource/banner.png"));  //横幅
        //中部
        jp1 = new JPanel(new GridLayout(3, 3, 25, 25));
        jp1.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));  //设置面板外边距
        jp1.setBackground(Color.decode("#E4F4FF"));  //设置面板颜色
        jp1_jl1 = new JLabel("账号: ", JLabel.CENTER);
        userIdTextField = new JTextField(30);  //账号输入框
        userIdTextField.addKeyListener(this);  //监听账号输入框,按回车即可登录
        jp1_jl2 = new JLabel("密码: ", JLabel.CENTER);
        passwordField = new JPasswordField(30);  //密码输入框
        passwordField.addKeyListener(this);  //监听密码输入框,按回车即可登录
        jp1_jl3 = new JLabel("注册新账号", JLabel.CENTER);
        jp1_jl3.addMouseListener(this);  //监听标签
        jp1_jl4 = new JLabel("找回密码", JLabel.CENTER);
        jp1_jl4.addMouseListener(this);  //监听标签
        jcBox = new JComboBox();  //状态
        jcBox.addItem("在线");
        jcBox.addItem("隐身");
        jcBox.addItem("忙碌");
        jcBox1 = new JCheckBox("记住密码");
        jcBox2 = new JCheckBox("自动登录");
        //设置字体
        Font font = new Font("宋体", 20, 20);
        jp1_jl1.setFont(font);
        jp1_jl2.setFont(font);
        jp1_jl3.setFont(font);
        jp1_jl4.setFont(font);
        jcBox.setFont(font);
        jcBox1.setFont(font);
        jcBox2.setFont(font);
        jp1_jl3.setForeground(Color.blue);  //字体颜色
        jp1_jl4.setForeground(Color.blue);
        jcBox.setBackground(Color.decode("#E4F4FF"));  //组件颜色
        jcBox1.setBackground(Color.decode("#E4F4FF"));
        jcBox2.setBackground(Color.decode("#E4F4FF"));
        //将组件放入面板,注意要按顺序放入
        jp1.add(jp1_jl1);  //账号
        jp1.add(userIdTextField);  //账号输入框
        jp1.add(jp1_jl3);  //注册新账号
        jp1.add(jp1_jl2);  //密码
        jp1.add(passwordField);  //密码输入框
        jp1.add(jp1_jl4);  //找回密码
        jp1.add(jcBox);  //状态
        jp1.add(jcBox1);  //记住密码
        jp1.add(jcBox2);  //自动登录
        //南部(优化:间距、字体大小等)
        jp2 = new JPanel();
        jp2.setBackground(Color.decode("#BFE1F9"));  //设置面板颜色
        jp2_jb1 = new JButton("设置");
        jp2_jb1.addMouseListener(this);  //监听设置按钮
        jp2_jb1.setPreferredSize(new Dimension(100, 30));  //设置按钮大小
        jp2_jb2 = new JButton("登录");
        jp2_jb2.addMouseListener(this);  //监听登录按钮
        jp2_jb2.setPreferredSize(new Dimension(100, 30));  //设置按钮大小
        //设置字体
        jp2_jb1.setFont(font);
        jp2_jb2.setFont(font);
        //将组件放入面板(优化:将两个按钮分别放在最左右)
        jp2.add(jp2_jb1);
        jp2.add(jp2_jb2);

        //设置窗口
        jf = new JFrame();
        //将组件、面板放入窗口
        jf.add(jl1, "North");  //北部
        jf.add(jp1, "Center");  //中部
        jf.add(jp2, "South");  //南部
        jf.setTitle("QQ2010 登录");  //标题
        ImageIcon icon = new ImageIcon("resource/icon.png");  //图标
        jf.setIconImage(icon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));  //图标大小
        jf.setSize(555, 420);  //窗口大小
        jf.setLocation(705, 360);  //窗口位置
        jf.setResizable(false);  //不可改变大小
        jf.setVisible(true);  //可见
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //关闭方式
        System.out.println("登录界面启动");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == jp2_jb2) {  //如果点击 登录 按钮
//        if(e.getClickCount() == 1){  //如果点击(登录按钮)一次; 此处若写成上一句,有时候要点两下才能登上
            //注意此处为局部变量
            String userId = userIdTextField.getText().trim();  //获取文本框的用户id
            String password = new String(passwordField.getPassword());  //获取密码框的用户密码
            //验证输入非空
            if (userId.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(this, "请输入账号或密码!");  //弹窗提示
            } else {
                //验证账号为纯数字
                if (!userId.matches("[0-9]+")) {  //如果不是纯数字
                    JOptionPane.showMessageDialog(this, "账号必须为纯数字!");
                } else {
                    userClientService = new UserClientService();  //调用用户服务类中的验证方法
                    if (userClientService.checkUser(userId, password)) {  //如果验证成功
                        //注意,要请求并显示在线好友的情况(在线头像亮,离线头像灰),需要先有 FriendsListView 对象,才能调用相关的方法(如 addFriendsList 等)
                        FriendsListView friendsListView = new FriendsListView(userId);//创建好友列表对象,打开好友列表; 参数:用户Id
                        ManageFriendsListView.addFriendsList(userId, friendsListView);  //将用户Id与其对应的好友列表添加到管理好友列表的类中
                        userClientService.onlineUser();  //向服务端发送请求在线好友列表的消息,注意这一步是在登录成功后打开好友列表前进行
                        jf.setVisible(false);  //关闭登录界面
                        System.out.println("用户 " + userId + " 登录成功");
                    } else {  //如果验证失败
                        JOptionPane.showMessageDialog(this, "用户名密码错误!");  //弹窗提示
                    }
                }
            }
        } else if (e.getSource() == jp1_jl3) {  //如果点击 注册新账号 标签
            new RegisterView();  //显示注册界面
            jf.setVisible(false);  //隐藏登录界面
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        if (e.getSource() == jp2_jb1) {  //当鼠标悬停在 设置 按钮上时
            jf.setCursor(handCursor);
        } else if (e.getSource() == jp2_jb2) {  //当鼠标悬停在 登录 按钮上时
            jf.setCursor(handCursor);
        } else if (e.getSource() == jp1_jl3) {  //当鼠标悬停在 注册新账号 标签上时
            jf.setCursor(handCursor);
        } else if (e.getSource() == jp1_jl4) {  //当鼠标悬停在 找回密码 标签上时
            jf.setCursor(handCursor);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Cursor nwResizeCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        if (e.getSource() == jp2_jb1) {  //当鼠标离开 设置 按钮上时
            jf.setCursor(nwResizeCursor);
        } else if (e.getSource() == jp2_jb2) {  //当鼠标离开 登录 按钮上时
            jf.setCursor(nwResizeCursor);
        } else if (e.getSource() == jp1_jl3) {  //当鼠标离开 注册新账号 标签上时
            jf.setCursor(nwResizeCursor);
        } else if (e.getSource() == jp1_jl4) {  //当鼠标离开 找回密码 标签上时
            jf.setCursor(nwResizeCursor);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //键盘按下触发事件
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            //注意此处为局部变量
            String userId = userIdTextField.getText().trim();  //获取文本框的用户id
            String password = new String(passwordField.getPassword());  //获取密码框的用户密码
            //验证非空
            if (userId.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(this, "请输入账号或密码!");  //弹窗提示
            } else {
                //验证账号为纯数字
                if (!userId.matches("[0-9]+")) {  //如果不是纯数字
                    JOptionPane.showMessageDialog(this, "账号必须为纯数字!");
                } else {
                    userClientService = new UserClientService();  //调用用户服务类中的验证方法
                    if (userClientService.checkUser(userId, password)) {  //如果验证成功
                        //注意,要请求并显示在线好友的情况(在线头像亮,离线头像灰),需要先有 FriendsListView 对象,才能调用相关的方法(如 addFriendsList 等)
                        FriendsListView friendsListView = new FriendsListView(userId);//创建好友列表对象,打开好友列表; 参数:用户Id
                        ManageFriendsListView.addFriendsList(userId, friendsListView);  //将用户Id与其对应的好友列表添加到管理好友列表的类中
                        userClientService.onlineUser();  //向服务端发送请求在线好友列表的消息,注意这一步是在登录成功后打开好友列表前进行
                        jf.setVisible(false);  //关闭登录界面
                        System.out.println("用户 " + userId + " 登录成功");
                    } else {  //如果验证失败
                        JOptionPane.showMessageDialog(this, "用户名密码错误!");  //弹窗提示
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

