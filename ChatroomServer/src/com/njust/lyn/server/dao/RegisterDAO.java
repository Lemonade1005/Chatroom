package com.njust.lyn.server.dao;

import com.njust.lyn.server.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lyn
 * @date 2022/9/9 19:50
 */
public class RegisterDAO {
    //验证是否是合法的用户注册(用户Id不重复)
    public boolean registerCheck(String userId) {
        boolean check = true;  //用于标记要注册的用户是否验证成功,默认true
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select user_id from user where user_id = ?";  //查询数据库的 user 表中的 user_id

        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            //给占位符赋值
            preparedStatement.setString(1, userId);  //
            //执行查询
            resultSet = preparedStatement.executeQuery();
            //遍历结果集
            while (resultSet.next()) {
                check = false;  //存在重复 userId, 验证失败
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放资源
            JDBCUtils.close(resultSet, preparedStatement, connection);
        }

        return check;
    }

    //将合法的用户在数据库中进行注册
    public void registerUser(String userId, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "insert into user(user_id, user_name, user_pwd) values(?, ?, ?)";  //将合法的注册用户添加到数据库的 user 表中

        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            //给占位符赋值
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, "新用户");
            preparedStatement.setString(3, password);
            //执行
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放资源
            JDBCUtils.close(null, preparedStatement, connection);
        }

    }
}
