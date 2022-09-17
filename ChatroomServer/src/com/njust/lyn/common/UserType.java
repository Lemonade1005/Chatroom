package com.njust.lyn.common;

/**
 * @author lyn
 * @date 2022/9/10 9:50
 * 用户类型接口,用于区别用户是登录还是注册
 */
public interface UserType {
    String USER_LOGIN = "1";  //登录请求
    String USER_REGISTER = "2";  //注册请求
}
