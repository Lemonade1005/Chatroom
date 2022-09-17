package com.njust.lyn.client.view;

import com.njust.lyn.client.service.ManageChatView;
import com.njust.lyn.client.service.UserClientService;
import com.njust.lyn.common.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author lyn
 * @date 2022/9/6 20:19
 * 展示用户好友、同学、朋友列表(原为: 我的好友、陌生人、黑名单)
 * 可优化:鼠标样式
 */
public class FriendsListView extends JFrame implements ActionListener, MouseListener {
    //我的好友部分
    JButton jpFriend_jb1, jpFriend_jb2, jpFriend_jb3;  //北部一个按钮,南部两个按钮
    JScrollPane friendJsp;  //中部一个滚动面板
    JPanel jpFriend, jpFriend2, jpFriend3;  //总的一个面板,中部一个面板,南部一个面板(注意北部不用面板)
    JLabel[] jlFriend;  //中部在线好友列表
    //陌生人部分
    JButton jpStranger_jb1, jpStranger_jb2, jpStranger_jb3;  //北部两个按钮,南部一个按钮
    JScrollPane StrangerJsp;  //中部一个滚动面板
    JPanel jpStranger, jpStranger2, jpStranger3;  //总的一个面板,中部一个面板,北部一个面板(注意南部不用面板)
    JLabel[] jlStranger;  //中部陌生人列表
    //黑名单部分
    JButton jpBlacklist_jb1, jpBlacklist_jb2, jpBlacklist_jb3;  //北部三个按钮
    JScrollPane blacklistJsp;  //中部一个滚动面板
    JPanel jpBlacklist, jpBlacklist2, jpBlacklist3;  //总的一个面板,北部一个面板,中部一个面板
    JLabel[] jlBlacklist;  //中部黑名单列表
    //卡片布局
    CardLayout cardLayout;
    JFrame jf;

    //监听事件中要使用到userId,将其设为属性
    String userId;

    public static void main(String[] args) {
        FriendsListView friendsListView = new FriendsListView("5");  //参数:用户Id
    }

    public void updateFriendsList(Message msg2) {
        String onlineFriends[] = msg2.getContent().split(" ");  //字符串以空格分割成为数组
        System.out.print("onlineFriends有: ");
        for (int i = 0; i < onlineFriends.length; i++) {
            System.out.print("onlineFriends[" + i + "]=" + onlineFriends[i] + " ");
        }

//        System.out.println();
//        for(int i = 0; i < jlFriend.length; i++){  //先初始化,全设置为灰色
//            jlFriend[i].setEnabled(false);
//        }
//        for(int i = 0; i < jlStranger.length; i++){  //先初始化,全设置为灰色
//            jlStranger[i].setEnabled(false);
//        }
//        for(int i = 0; i < jlBlacklist.length; i++){  //先初始化,全设置为灰色
//            jlBlacklist[i].setEnabled(false);
//        }

        //设置头像为彩色
        //Integer.parseInt(str) 表示将 String 类型转换为 int 类型
        //由于遍历从0开始,所以要减1
        for (int i = 0; i < onlineFriends.length; i++) {  //循环处理在线用户(我的好友)
            jlFriend[Integer.parseInt(onlineFriends[i]) - 1].setEnabled(true);  //与在线好友Id相同的标签(即好友头像)
            //有bug,我的好友头像亮时,对应的陌生人和黑名单也会亮
//            jlStranger[Integer.parseInt(onlineFriends[i]) - 1].setEnabled(true);  //与在线陌生人Id相同的标签(即陌生人头像)
//            jlBlacklist[Integer.parseInt(onlineFriends[i]) - 1].setEnabled(true);  //与在线黑名单Id相同的标签(即黑名单头像)
//            if (!onlineFriends[i].equals(i + 1)) {
//                jlFriend[i + 1].setEnabled(false);
//            }
        }
    }
    //根据服务端回复的消息中的内容(在线好友的Id)更新好友列表

    //有参构造函数
    public FriendsListView(String userId) {
        this.userId = userId;  //为userId这个属性赋值,使其作为全局变量

        //大小735*370,位置1500*60
        /*
        我的好友部分
         */
        //北部
        jpFriend_jb1 = new JButton("好友");
        //中部
        jpFriend2 = new JPanel(new GridLayout(50, 1, 20, 20));  //网格布局
        //假设有50个好友
        jlFriend = new JLabel[50];
        for (int i = 0; i < jlFriend.length; i++) {
            jlFriend[i] = new JLabel(i + 1 + "", new ImageIcon("resource/avatar1.png"), JLabel.LEFT);  //头像
            jlFriend[i].setEnabled(false);  //默认头像为灰色,在更新好友列表的 updateFriendsList() 方法中设置为彩色
            jpFriend2.add(jlFriend[i]);  //放入面板
            jlFriend[i].addMouseListener(this);  //监听好友卡片中的好友标签
        }
        friendJsp = new JScrollPane(jpFriend2);  //放入滚动面板
        //南部
        jpFriend3 = new JPanel(new GridLayout(2, 1));
        jpFriend_jb2 = new JButton("同学");
        jpFriend_jb2.addActionListener(this);  //监听好友卡片中的陌生人按钮
        jpFriend_jb3 = new JButton("老师");
        jpFriend_jb3.addActionListener(this);  //监听好友卡片中的黑名单按钮
        //放入面板
        jpFriend3.add(jpFriend_jb2);
        jpFriend3.add(jpFriend_jb3);
        //放入总的面板
        jpFriend = new JPanel(new BorderLayout());
        jpFriend.add(jpFriend_jb1, "North");  //北部直接放入按钮
        jpFriend.add(friendJsp, "Center");
        jpFriend.add(jpFriend3, "South");


        /*
        陌生人部分
         */
        //北部
        jpStranger2 = new JPanel(new GridLayout(2, 1));
        jpStranger_jb1 = new JButton("好友");
        jpStranger_jb1.addActionListener(this);  //监听陌生人卡片中的好友按钮
        jpStranger_jb2 = new JButton("同学");
        //放入面板
        jpStranger2.add(jpStranger_jb1);
        jpStranger2.add(jpStranger_jb2);
        //中部
        jpStranger3 = new JPanel(new GridLayout(30, 2, 20, 20));  //网格布局
        //假设有30个陌生人
        jlStranger = new JLabel[30];
        for (int i = 0; i < jlStranger.length; i++) {
            jlStranger[i] = new JLabel(i + 51 + "", new ImageIcon("resource/avatar2.png"), JLabel.LEFT);  //头像
            jlStranger[i].setEnabled(false);  //默认头像为灰色,在更新好友列表的 updateFriendsList() 方法中设置为彩色
            jpStranger3.add(jlStranger[i]);  //放入面板
            jlStranger[i].addMouseListener(this);  //监听陌生人卡片中的陌生人标签
        }
        StrangerJsp = new JScrollPane(jpStranger3);  //放入滚动面板
        //南部
        jpStranger_jb3 = new JButton("老师");
        jpStranger_jb3.addActionListener(this);  //监听陌生人卡片中的黑名单按钮

        //放入总的面板
        jpStranger = new JPanel(new BorderLayout());
        jpStranger.add(jpStranger2, "North");
        jpStranger.add(StrangerJsp, "Center");
        jpStranger.add(jpStranger_jb3, "South");  //南部直接放入按钮


        /*
        黑名单部分
         */
        //北部
        jpBlacklist2 = new JPanel(new GridLayout(3, 1));
        jpBlacklist_jb1 = new JButton("好友");
        jpBlacklist_jb1.addActionListener(this);  //监听黑名单卡片中的好友按钮
        jpBlacklist_jb2 = new JButton("同学");
        jpBlacklist_jb2.addActionListener(this);  //监听黑名单卡片中的陌生人按钮
        jpBlacklist_jb3 = new JButton("老师");
        //放入面板
        jpBlacklist2.add(jpBlacklist_jb1);
        jpBlacklist2.add(jpBlacklist_jb2);
        jpBlacklist2.add(jpBlacklist_jb3);
        //中部
        jpBlacklist3 = new JPanel(new GridLayout(10, 1, 20, 20));  //网格布局
        //假设有10个黑名单
        jlBlacklist = new JLabel[10];
        for (int i = 0; i < jlBlacklist.length; i++) {
            jlBlacklist[i] = new JLabel(i + 61 + "", new ImageIcon("resource/avatar3.png"), JLabel.LEFT);  //头像
            jlBlacklist[i].setEnabled(false);  //默认头像为灰色,在更新好友列表的 updateFriendsList() 方法中设置为彩色
            jpBlacklist3.add(jlBlacklist[i]);  //放入面板
            jlBlacklist[i].addMouseListener(this);  //监听黑名单卡片中的黑名单标签
        }
        blacklistJsp = new JScrollPane(jpBlacklist3);  //放入滚动面板
        //放入总的面板
        jpBlacklist = new JPanel(new BorderLayout());
        jpBlacklist.add(jpBlacklist2, "North");
        jpBlacklist.add(blacklistJsp, "Center");

//        //监听所有按钮
//        jpFriend_jb1.addMouseListener(this);  //监听好友卡片中的好友按钮
//        jpFriend_jb1.addMouseListener(this);  //监听好友卡片中的陌生人按钮
//        jpFriend_jb1.addMouseListener(this);  //监听好友卡片中的黑名单按钮
//        jpStranger_jb3.addMouseListener(this);  //监听陌生人卡片中的好友按钮
//        jpStranger_jb3.addMouseListener(this);  //监听陌生人卡片中的陌生人按钮
//        jpStranger_jb3.addMouseListener(this);  //监听陌生人卡片中的黑名单按钮
//        jpBlacklist_jb1.addMouseListener(this);  //监听黑名单卡片中的好友按钮
//        jpBlacklist_jb2.addMouseListener(this);  //监听黑名单卡片中的陌生人按钮
//        jpBlacklist_jb3.addMouseListener(this);  //监听黑名单卡片中的黑名单按钮


        //设置字体
        Font font = new Font("宋体", 20, 30);
        jpFriend_jb1.setFont(font);
        jpFriend_jb2.setFont(font);
        jpFriend_jb3.setFont(font);
        jpStranger_jb1.setFont(font);
        jpStranger_jb2.setFont(font);
        jpStranger_jb3.setFont(font);
        jpBlacklist_jb1.setFont(font);
        jpBlacklist_jb2.setFont(font);
        jpBlacklist_jb3.setFont(font);
        for (JLabel jLabel : jlFriend) {
            jLabel.setFont(font);
        }
        for (JLabel jLabel : jlStranger) {
            jLabel.setFont(font);
        }
        for (JLabel jLabel : jlBlacklist) {
            jLabel.setFont(font);
        }

        //设置颜色
        jpFriend_jb1.setBackground(Color.decode("#c3cfe2"));
        jpFriend_jb2.setBackground(Color.decode("#c3cfe2"));
        jpFriend_jb3.setBackground(Color.decode("#c3cfe2"));
        jpStranger_jb1.setBackground(Color.decode("#c3cfe2"));
        jpStranger_jb2.setBackground(Color.decode("#c3cfe2"));
        jpStranger_jb3.setBackground(Color.decode("#c3cfe2"));
        jpBlacklist_jb1.setBackground(Color.decode("#c3cfe2"));
        jpBlacklist_jb2.setBackground(Color.decode("#c3cfe2"));
        jpBlacklist_jb3.setBackground(Color.decode("#c3cfe2"));
        jpFriend.setBackground(Color.decode("#fdfcfb"));
        jpFriend2.setBackground(Color.decode("#fdfcfb"));
        jpFriend3.setBackground(Color.decode("#fdfcfb"));
        jpStranger.setBackground(Color.decode("#fdfcfb"));
        jpStranger2.setBackground(Color.decode("#fdfcfb"));
        jpStranger3.setBackground(Color.decode("#fdfcfb"));
        jpBlacklist.setBackground(Color.decode("#fdfcfb"));
        jpBlacklist2.setBackground(Color.decode("#fdfcfb"));
        jpBlacklist3.setBackground(Color.decode("#fdfcfb"));

        //窗口设置
        jf = new JFrame("欢迎您,用户 " + userId);  //userId为这个类传入的参数
        cardLayout = new CardLayout();
        jf.setLayout(cardLayout);  //整个JFrame设置为卡片布局
        jf.setSize(330, 735);
        jf.setLocation(1500, 60);
        ImageIcon icon = new ImageIcon("resource/icon.png");  //图标
        jf.setIconImage(icon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));  //图标大小
        //放入窗口
        jf.add(jpFriend, "1");  //好友列表为卡片1
        jf.add(jpStranger, "2");  //陌生人列表为卡片2
        jf.add(jpBlacklist, "3");  //黑名单列表为卡片3
        jf.setVisible(true);
        jf.setResizable(false);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        //监听窗口,点击关闭时结束进程
//        jf.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                UserClientService userClientService = new UserClientService();
//                userClientService.logout();
//                jf.dispose();
//            }
//        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jpFriend_jb2) {  //如果点击了第一个面板jpFriend中的陌生人按钮
            cardLayout.show(jf.getContentPane(), "2");  //显示第二张卡片
        } else if (e.getSource() == jpFriend_jb3) {  //如果点击了第一个面板jpFriend中的黑名单按钮
            cardLayout.show(jf.getContentPane(), "3");  //显示第三张卡片
        } else if (e.getSource() == jpStranger_jb1) {  //如果点击了第二个面板jpStranger中的好友按钮
            cardLayout.show(jf.getContentPane(), "1");  //显示第一张卡片
        } else if (e.getSource() == jpStranger_jb3) {  //如果点击了第二个面板jpStranger中的黑名单按钮
            cardLayout.show(jf.getContentPane(), "3");  //显示第三张卡片
        } else if (e.getSource() == jpBlacklist_jb1) {  //如果点击了第三个面板jpBlacklist中的好友按钮
            cardLayout.show(jf.getContentPane(), "1");  //显示第一张卡片
        } else if (e.getSource() == jpBlacklist_jb2) {  //如果点击了第三个面板jpBlacklist中的陌生人按钮
            cardLayout.show(jf.getContentPane(), "2");  //显示第二张卡片
        } else {

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {  //如果双击两次
            //得到聊天好友的Id
            String friendId = ((JLabel) e.getSource()).getText();  //已知获取到的标签,强制转换
            ChatView chatView = new ChatView(this.userId, friendId);//创建聊天界面对象,打开聊天界面
            //把聊天界面加入到管理聊天界面的 HashMap 中(会方便之后的与在线用户聊天功能)
            ManageChatView.addChatView(this.userId + friendId, chatView);  //例如聊天界面的唯一标识 userId_friendId 为 12,则表明该窗口是用户 1 对用户 2 说
//            ManageChatView.addChatView(friendId + this.userId, chatView);  //将聊天双方的聊天界面加入到集合中,方便取出并显示消息
            System.out.println("添加的userId_friendId为: " + this.userId + friendId);
            System.out.println("用户 " + this.userId + " 正在和用户 " + friendId + " 聊天");
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
        //设置鼠标在标签上的颜色效果
        JLabel jLabel = (JLabel) e.getSource();
        jLabel.setForeground(Color.red);
//        //设置鼠标在按钮上的样式
//        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
//        if(e.getSource() == jpFriend_jb1 || e.getSource() == jpFriend_jb2 || e.getSource() == jpFriend_jb3
//                || e.getSource() == jpStranger_jb1 || e.getSource() == jpStranger_jb2 || e.getSource() == jpStranger_jb3
//                || e.getSource() == jpBlacklist_jb1 ||  e.getSource() == jpBlacklist_jb2 || e.getSource() == jpBlacklist_jb3 ){
//            jf.setCursor(handCursor);
//        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //设置鼠标离开标签的颜色效果
        JLabel jLabel = (JLabel) e.getSource();
        jLabel.setForeground(Color.black);
//        //设置鼠标离开按钮的样式
//        Cursor nwResizeCursor = new Cursor(Cursor.DEFAULT_CURSOR);
//        if(e.getSource() == jpFriend_jb1 || e.getSource() == jpFriend_jb2 || e.getSource() == jpFriend_jb3
//                || e.getSource() == jpStranger_jb1 || e.getSource() == jpStranger_jb2 || e.getSource() == jpStranger_jb3
//                || e.getSource() == jpBlacklist_jb1 ||  e.getSource() == jpBlacklist_jb2 || e.getSource() == jpBlacklist_jb3 ){
//            jf.setCursor(nwResizeCursor);
//        }
    }

}
