package com.njust.lyn.common;

import java.io.Serializable;

/**
 * @author lyn
 * @date 2022/9/2 11:50
 * 客户端与服务端共有的用户对象，用于通信
 */
public class User implements Serializable {  //为了进行序列化，需要实现Serializable 接口
    private static final long serialVersionUID = 1L;  //为了提高序列化的兼容性
    private String userId;  //用户Id
    private String password;  //密码
    private String userType;  //区别用户登录还是注册

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
