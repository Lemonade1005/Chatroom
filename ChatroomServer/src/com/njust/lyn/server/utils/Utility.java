package com.njust.lyn.server.utils;

import java.util.Scanner;
//import java.util.*;


/**
 * @author
 * @date 2022/9/2 12:26
 * 工具类的作用:
 * 处理各种情况的用户输入,并且能按照程序员对的需求,得到用户的控制台输入
 */

@SuppressWarnings({"all"})
public class Utility {
    //静态属性
    private static Scanner scanner = new Scanner(System.in);


    /**
     * 功能:读取键盘输入的一个菜单项,值:1-5范围
     */
    public static char readMenuSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false);
            c = str.charAt(0);//将字符串转换为字符
            if (c != '1' && c != '2' && c != '3' && c != '4' && c != '5') {
                System.out.println("选择错误，请重新输入：");
            } else break;
        }
        return c;
    }

    /**
     * 功能:读取键盘输入的一个字符
     */
    public static char readChar() {
        String str = readKeyBoard(1, false);//就是一个字符
        return str.charAt(0);
    }

    /**
     * 功能;读取键盘输入的一个字符,如果直接回车,则返回指定的默认值
     * defaultValue 指定的默认值
     * return 默认值或者输入的字符
     */
    public static char readChar(char defaultValue) {
        String str = readKeyBoard(1, true);//要么是空字符,要么??
        return str.length() == 0 ? defaultValue : str.charAt(0);
    }


    /**
     * 读取键盘输入的整型,长度少于两位
     * return 整数
     */
    public static int readInt() {
        int n;
        for (; ; ) {
            String str = readKeyBoard(2, false);
            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.println("选择错误，请重新输入：");
            }
        }
        return n;
    }

    /**
     * 功能:读取键盘输入的整数或者默认值,如果直接回车,则返回默认值
     * defaultValue 指定的默认值
     * return 整数或者默认值
     */
    public static int readInt(int defaultValue) {
        int n;
        for (; ; ) {
            String str = readKeyBoard(2, false);
            if (str.equals("")) {
                return defaultValue;
            }

            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.println("选择错误，请重新输入：");
            }
        }
        return n;
    }

    /**
     * 功能:读取键盘输入的指定长度的字符串
     * limit:限制的长度
     * return:指定长度的字符串
     */
    public static String readString(int limit) {
        return readKeyBoard(limit, false);
    }

    /**
     * 功能:读取键盘输入的指定长度的字符串或者默认值,如果直接回车,返回默认值
     * limit:限制的长度
     * defaultValue 指定的默认值
     * return 指定长度的字符串
     */
    public static String readString(int limit, String defaultValue) {
        String str = readKeyBoard(limit, true);
        return str.equals("") ? defaultValue : str;
    }

    /**
     * 功能:读取键盘输入的确认选项,Y或N
     * 将小的功能,封装到一个方法中
     * return Y/N
     */
    public static char readConfirmSelection() {
        System.out.println("请输入你的选择(Y/N)");
        char c;
        for (; ; ) {//无限循环
            //在这里,将接收到字符,转换程大写字母
            //y=>Y,n=>N
            String str = readKeyBoard(1, false).toUpperCase();
            c = str.charAt(0);
            if (c == 'Y' || c == 'N') {
                break;
            } else {
                System.out.print("选择错误，请重新输入：");
            }
        }
        return c;
    }

    /**
     * 功能:读取键盘输入,如果输入为空,或者输入大于limit的长度,就会提示重新输入
     */
    private static String readKeyBoard(int limit, boolean blankReturn){
        //定义了字符串
        String line="";

        //scanner.hasNextLine()判断有没有下一行
        while (scanner.hasNextLine()){
            line=scanner.nextLine();//读取下一行

            //如果Line.length=0,即用户没有输入任何内容,直接回车
            if(line.length()==0){
                if(blankReturn) return line;
                else continue;//如果blackRuturn=false,不接受空串,必须输入内容
            }

            //如果用户输入的内容大于了limit,就提示重写输入
            //如果用户输入的内容哦个>0<=limit,我就接受
            if(line.length()<1 || line.length()>limit){
                System.out.println("输入长度（不大于 "+ limit + "）错误，请重新输入：");
                continue;
            }
            break;
        }
        return line;
    }

}