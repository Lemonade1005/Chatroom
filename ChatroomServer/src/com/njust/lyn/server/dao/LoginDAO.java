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
public class LoginDAO {
    //验证是否是合法的用户登录(用户Id、密码均正确)
    public boolean LoginCheck(String userId, String password) {
        boolean check = false;  //用于标记要登录的用户是否验证成功,默认false
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select user_id from user where user_id = ? and user_pwd = ?";

        try {
            //获得连接
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            //给占位符赋值
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, password);
            //执行查询
            resultSet = preparedStatement.executeQuery();
            //遍历结果集
            while (resultSet.next()){
                check = true;  //验证成功
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放资源
            JDBCUtils.close(resultSet, preparedStatement, connection);
        }

        return check;
    }
}
