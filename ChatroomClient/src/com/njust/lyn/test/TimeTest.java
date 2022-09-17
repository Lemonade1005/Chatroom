package com.njust.lyn.test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lyn
 * @date 2022/9/8 11:17
 * 获取格式化的年-月-日  时-分-秒
 */
public class TimeTest {
    public static void main(String[] args) {
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");  //注: hh表示12小时制; HH表示24小时制
        System.out.println(dateFormat.format(date));
    }
}
