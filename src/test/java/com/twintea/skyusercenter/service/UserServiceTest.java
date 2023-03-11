package com.twintea.skyusercenter.service;

import com.twintea.skyusercenter.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void test() {
        User user = new User();
        user.setUserName("testUser");
        user.setUserAccount("testtest");
        user.setAvatarUrl("http://baidu/lala.png");
        user.setGender(0);
        user.setUserPassword("mmm123");
        user.setEmail("fff");
        user.setUserStatus(0);
        user.setPhone("123456");

        userService.save(user);
        System.out.println(user.getId());

    }

    @Test
    void testShow(){
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userService.list();
        Assert.assertEquals(1, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    void userRegister() {
        //非空
        String userAccount = "twintea";
        String userPassword = " ";
        String checkPassword = "12345678";
        String registerCode = "7k7j";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, registerCode);
        Assertions.assertEquals(-1,result);
        //账户长度不小于4位
         userPassword = "12345678";
         userAccount="abc";
         result = userService.userRegister(userAccount, userPassword, checkPassword, registerCode);
        Assertions.assertEquals(-1,result);
        //密码不小于8位
        userAccount = "twintea";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, registerCode);
        Assertions.assertEquals(-1,result);
        //密码和二次输入密码必须一致
        userAccount="abcd";
        userPassword = "123456";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, registerCode);
        Assertions.assertEquals(-1,result);
        //用户账户不能包含特殊字符
        userPassword = "12345678";
        userAccount="ab+cd";
        result = userService.userRegister(userAccount, userPassword, checkPassword, registerCode);
        Assertions.assertEquals(-1,result);
        //用户注册输入的账户不能与数据库中存在的账户相同
        userAccount="testtest";
        result = userService.userRegister(userAccount, userPassword, checkPassword, registerCode);
        Assertions.assertEquals(-1,result);
        //正常数据
//        userAccount="ttttt";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertTrue(result>0);
        //密码不能包含空格
        userAccount="oooooo";
        userPassword="123 45678 9";
        checkPassword="123 45678 9";
        result = userService.userRegister(userAccount, userPassword, checkPassword, registerCode);
        Assertions.assertEquals(-1,result);
    }
}