package com.njust.lyn.server.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * @author lyn
 * @date 2022/9/9 11:00
 * 封装JDBC的工具类
 */
public class JDBCUtils {
    private static String user;  //用户名
    private static String password;  //密码
    private static String url;  //数据库url
    private static String driver;  //驱动名

    //初始化
    static {  //连接、释放只需要执行一次,故使用静态代码块
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream("src//sql.properties"));
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            url = properties.getProperty("url");
            driver = properties.getProperty("driver");
        } catch (IOException e) {
            //在实际开发中,将打印异常的语句换成这样,这样是为了:
            //1、将编译异常转变为运行异常
            //2、调用者可以选择捕获该异常,也可以选择默认处理该异常,比较方便
            throw new RuntimeException(e);
        }
    }

    //连接数据库
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {  //操作同上
            throw new RuntimeException(e);
        }
    }

    //关闭相关资源(先用后关)
    //ResultSet 结果集、Statement 或 PrepareStatement 执行对象、Connection 连接
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            //判断非空,关闭结果集
            if(resultSet != null) {
                resultSet.close();
            }
            //判断非空,关闭执行对象
            if(resultSet != null) {
                statement.close();
            }
            //判断非空,关闭连接
            if(resultSet != null) {
                connection.close();
            }
        } catch (SQLException e) {  //操作同上
            throw new RuntimeException(e);
        }

    }
}
