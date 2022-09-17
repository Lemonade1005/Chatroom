//package com.njust.lyn.client.view;
//
//import com.njust.lyn.client.service.ChatClientService;
//import com.njust.lyn.client.service.FileClientService;
//import com.njust.lyn.client.service.UserClientService;
//import com.njust.lyn.client.utils.Utility;
//
///**
// * @author lyn
// * @date 2022/9/2 12:11
// * 客户端的展示界面(控制台形式)
// */
//public class ViewConsole {
//    public static void main(String[] args) {
//        //测试
//        new ViewConsole().mainMenu();
//        System.out.println("客户端退出....");
//    }
//
//    private boolean loop = true;  //控制菜单的循环
//    private String key;  //用户键盘输入的命令
//    private UserClientService userClientService = new UserClientService();  //为了直接使用UserClientService 类中的方法,将这个对象作为属性
//    private ChatClientService chatClientService = new ChatClientService();  //为了直接使用ChatClientService 类中的方法,将这个对象作为属性
//    private FileClientService fileClientService = new FileClientService();  //为了直接使用FileClientService 类中的方法,将这个对象作为属性
//
//    //展示主菜单
//    private void mainMenu() {
//        while (loop) {
//            System.out.println("====================欢迎使用聊天室====================");
//            System.out.println("====================您的选择是=======================");
//            System.out.println("                    1:登录                   ");
//            System.out.println("                    9:退出                   ");
//            System.out.print("请输入: ");
//
//            key = Utility.readString(1);  //限制用户输入字符串长度不超过1,并读入
//
//            //根据用户的输入,处理不同的情况
//            switch (key) {
//                case "1":
//                    System.out.println("======================登录系统=======================");
//                    System.out.print("请输入用户名: ");
//                    String userId = Utility.readString(50);
//                    System.out.print("请输入密码: ");
//                    String password = Utility.readString(50);
//                    //使用UserClientService 类中的checkUser() 到服务端验证用户名和密码,获得Message对象的msgType即可判断能否登录成功
//                    if (userClientService.checkUser(userId, password)) {  //登录成功,将UserClientService 对象作为属性即可调用这个对象的方法
//                        System.out.println("=====================欢迎您,用户" + userId + "====================");
//                        while (loop) {
//                            System.out.println("=====================您的选择是======================");
//                            System.out.println("                     1:查看在线用户                    ");
//                            System.out.println("                     2:群发消息                    ");
//                            System.out.println("                     3:私聊消息                    ");
//                            System.out.println("                     4:发送文件                    ");
//                            System.out.println("                     5:离线消息                    ");
//                            System.out.println("                     6:公告推送                    ");
//                            System.out.println("                     9:退出登录                    ");
//                            System.out.print("请输入: ");
//
//                            key = Utility.readString(1);  //限制用户输入1个字符串,并读入
//
//                            //根据用户的输入,调用不同的方法处理对应的情况,进入二级菜单 (代码还没写完)
//                            switch (key) {
//                                case "1":  //在UserClientSService 类中写一个获取在线用户列表的方法
//                                    userClientService.onlineUser();
////                                    System.out.println("查看在线用户");
////                                    loop = false;  //这里还有bug,显示在线用户列表跑到再次弹出的二级菜单之后了
//                                    break;
//                                case "2":  //在ChatClientService 类中写一个群聊的方法
//                                    chatClientService.groupChat(userId);
////                                    System.out.println("群发消息");
//                                    break;
//                                case "3":  //在ChatClientService 类中写一个私聊的方法
//                                    String friendId = "";
//                                    String content = "";
//                                    chatClientService.privateChat(userId, friendId, content);  //传入senderId,而getterId和content在方法中读取
////                                    System.out.println("私聊消息");
//                                    break;
//                                case "4":  //在FileClientService 类中写一个发送文件的方法
//                                    fileClientService.sendFile(userId);
////                                    System.out.println("发送文件");
//                                    break;
//                                case "9":  //在UserClientSService 类中写一个告知服务器结束进程的方法
//                                    userClientService.logout();
//                                    loop = false;  //退出界面的循环
//                                    break;
//                            }
//                        }
//
//                    } else {  //登录失败
//                        System.out.println("====================用户名或密码错误====================");
//                        System.out.println();  //换行
//                    }
//                    break;
//                case "9":
//                    System.out.println("退出成功....");
//                    loop = false;  //退出总界面的循环
//            }
//        }
//
//    }
//}
