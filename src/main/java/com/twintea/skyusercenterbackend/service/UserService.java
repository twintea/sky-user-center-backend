package com.twintea.skyusercenterbackend.service;

import com.twintea.skyusercenterbackend.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Pixar
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-03-05 02:34:31
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 二次输入密码
     * @return 新用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword,String registerCode);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户数据脱敏
     * @param OriginalUser 原始用户数据
     * @return
     */
    User getSafetyUser(User OriginalUser);

    /**
     * 用户注销
     * @param request
     */
    Boolean userLogout(HttpServletRequest request);
}
