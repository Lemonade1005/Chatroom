package com.njust.lyn.client.view;

import com.njust.lyn.client.service.UserClientService;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author lyn
 * @date 2022/9/10 0:01
 * 展示用户注册界面
 */
public class RegisterView extends JFrame implements MouseListener, KeyListener, FocusListener {
    //窗口大小555*420, 位置705*360
    //布局:北部一个JLabel,中部一个JPanel,南部一个JPanel
    JFrame jf;
    JLabel jl1, jp1_jl1, jp1_jl2, jp1_jl3, jp1_jl4, jp1_jl5, jp1_jl6;  //北部1个标签,中部6个标签
    JPanel jp1, jp2;  //中部和南部的面板
    JTextField userIdTextField;  //文本框
    JPasswordField passwordField, passwordVerifyField;  //密码框,确认密码框
    JButton jp2_jb1;  //按钮

    public static void main(String[] args) {
        new RegisterView();
    }

    public RegisterView() {
        //北部
        jl1 = new JLabel(new ImageIcon("resource/banner.png"));  //横幅
        //中部
        jp1 = new JPanel(new GridLayout(3, 3, 25, 25));
        jp1.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));  //设置面板外边距
        jp1.setBackground(Color.decode("#E4F4FF"));  //设置面板颜色
        jp1_jl1 = new JLabel("账号: ", JLabel.CENTER);
        userIdTextField = new JTextField(30);  //账号输入框
        userIdTextField.addFocusListener(this);  //监听账号输入框,点击会提示文字
        userIdTextField.addKeyListener(this);  //监听账号输入框,按回车即可注册
        jp1_jl2 = new JLabel("请输入账号", JLabel.CENTER);
        jp1_jl2.setVisible(false);  //提示标签默认不可见
        jp1_jl3 = new JLabel("密码: ", JLabel.CENTER);
        passwordField = new JPasswordField(30);  //密码输入框
        passwordField.addFocusListener(this);  //监听密码输入框,点击会提示文字
        passwordField.addKeyListener(this);  //监听密码输入框,按回车即可注册
        jp1_jl4 = new JLabel("请输入密码", JLabel.CENTER);
        jp1_jl4.setVisible(false);  //提示标签默认不可见
        jp1_jl5 = new JLabel("确认密码: ", JLabel.CENTER);
        passwordVerifyField = new JPasswordField(30);  //确认密码输入框
        passwordVerifyField.addFocusListener(this);  //监听确认密码输入框,点击会提示文字
        passwordVerifyField.addKeyListener(this);  //监听确认密码输入框,按回车即可注册
        jp1_jl6 = new JLabel("再次输入密码", JLabel.CENTER);
        jp1_jl6.setVisible(false);  //提示标签默认不可见
        //设置字体
        Font font = new Font("宋体", 20, 20);
        jp1_jl1.setFont(font);
        jp1_jl2.setFont(font);
        jp1_jl3.setFont(font);
        jp1_jl4.setFont(font);
        jp1_jl5.setFont(font);
        jp1_jl6.setFont(font);
        jp1_jl2.setForeground(Color.decode("#878B8E"));  //设置提示文字颜色
        jp1_jl4.setForeground(Color.decode("#878B8E"));
        jp1_jl6.setForeground(Color.decode("#878B8E"));
        //将组件放入面板,注意要按顺序放入
        jp1.add(jp1_jl1);
        jp1.add(userIdTextField);
        jp1.add(jp1_jl2);
        jp1.add(jp1_jl3);
        jp1.add(passwordField);
        jp1.add(jp1_jl4);
        jp1.add(jp1_jl5);
        jp1.add(passwordVerifyField);
        jp1.add(jp1_jl6);
        //南部
        jp2 = new JPanel();
        jp2.setBackground(Color.decode("#BFE1F9"));  //设置面板颜色
        jp2_jb1 = new JButton("立即注册");
        jp2_jb1.addMouseListener(this);  //监听注册按钮
        jp2_jb1.addKeyListener(this);  //监听注册按钮
        //设置字体
        jp2_jb1.setFont(font);
        //将组件放入面板
        jp2.add(jp2_jb1);

        //设置窗口
        jf = new JFrame();
        //将组件、面板放入窗口
        jf.add(jl1, "North");  //北部
        jf.add(jp1, "Center");  //中部
        jf.add(jp2, "South");  //南部
        jf.setTitle("QQ2010 注册");  //标题
        ImageIcon icon = new ImageIcon("resource/icon.png");  //图标
        jf.setIconImage(icon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));  //图标大小
        jf.setSize(555, 420);  //窗口大小
        jf.setLocation(705, 360);  //窗口位置
        jf.setResizable(false);  //不可改变大小
        jf.setVisible(true);  //可见
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //关闭方式
        System.out.println("注册界面启动");

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            String userId = userIdTextField.getText();
            String password = new String(passwordField.getPassword());
            String passwordVerify = new String(passwordVerifyField.getPassword());
            UserClientService userClientService = new UserClientService();  //创建服务类的对象
            //验证输入非空
            if (userId.equals("") || password.equals("") || passwordVerify.equals("")) {
                JOptionPane.showMessageDialog(this, "请填写完整!");  //弹窗提示
            } else {
                //验证账号为纯数字
                if (!userId.matches("[0-9]+")) {  //如果不是纯数字
                    JOptionPane.showMessageDialog(this, "账号必须为纯数字!");
                } else {
                    //验证密码与确认密码保持一致
                    if (!password.equals(passwordVerify)) {  //如果密码与确认密码不一致
                        JOptionPane.showMessageDialog(this, "请保证密码与确认密码一致!");
                    } else {
                        if (userClientService.RegisterUser(userId, password)) {  //注册成功
                            JOptionPane.showMessageDialog(this, "注册成功!");  //弹窗提示
                            new LoginView();  //显示登录界面
                            jf.setVisible(false);  //隐藏注册界面
                        } else {  //注册失败
                            JOptionPane.showMessageDialog(this, "该账号已注册!");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == jp2_jb1) {  //如果点击注册按钮
            String userId = userIdTextField.getText();
            String password = new String(passwordField.getPassword());
            String passwordVerify = new String(passwordVerifyField.getPassword());
            UserClientService userClientService = new UserClientService();  //创建服务类的对象
            //验证输入非空
            if (userId.equals("") || password.equals("") || passwordVerify.equals("")) {
                JOptionPane.showMessageDialog(this, "请填写完整!");  //弹窗提示
            } else {
                //验证账号为纯数字
                if (!userId.matches("[0-9]+")) {  //如果不是纯数字
                    JOptionPane.showMessageDialog(this, "账号必须为纯数字!");
                } else {
                    //验证密码与确认密码保持一致
                    if (!password.equals(passwordVerify)) {  //如果密码与确认密码不一致
                        JOptionPane.showMessageDialog(this, "请保证密码与确认密码一致!");
                    } else {
                        if (userClientService.RegisterUser(userId, password)) {  //注册成功
                            JOptionPane.showMessageDialog(this, "注册成功!");  //弹窗提示
                            new LoginView();  //显示登录界面
                            jf.setVisible(false);  //隐藏注册界面
                        } else {  //注册失败
                            JOptionPane.showMessageDialog(this, "该账号已注册!");
                        }
                    }
                }
            }
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
        if (e.getSource() == jp2_jb1) {  //当鼠标悬停在 注册 按钮上时
            jf.setCursor(handCursor);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Cursor nwResizeCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        if (e.getSource() == jp2_jb1) {  //当鼠标离开 注册 按钮上时
            jf.setCursor(nwResizeCursor);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        //点击对应输入框会显示对应提示
        if (e.getSource() == userIdTextField) {
            jp1_jl2.setVisible(true);
        } else if (e.getSource() == passwordField) {
            jp1_jl4.setVisible(true);
        } else if (e.getSource() == passwordVerifyField) {
            jp1_jl6.setVisible(true);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        //离开对应输入框会隐藏对应提示
        if (e.getSource() == userIdTextField) {
            jp1_jl2.setVisible(false);
        } else if (e.getSource() == passwordField) {
            jp1_jl4.setVisible(false);
        } else if (e.getSource() == passwordVerifyField) {
            jp1_jl6.setVisible(false);
        }
    }
}
