package com.twintea.skyusercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twintea.skyusercenter.common.ErrorCode;
import com.twintea.skyusercenter.domain.User;
import com.twintea.skyusercenter.exception.BusinessException;
import com.twintea.skyusercenter.service.UserService;
import com.twintea.skyusercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.twintea.skyusercenter.constant.UserConstant.USER_LOGIN_STATE;
import static com.twintea.skyusercenter.constant.UserConstant.VALID_STATUS;

/**
 * @author Pixar
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-03-05 02:34:31
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆加密
     */
    private final String SALT = "skyusercenter";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String registerCode) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, registerCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userPassword.contains(" ") || checkPassword.contains(" ")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能输入空格");

        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户小于4位");

        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于8位");

        }

        //用户输入账户不能包含特殊字符
        String validPatter = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatter).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        //校验密码和二次输入密码
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不一致");
        }

        //用户注册输入的账户不能与数据库中存在的账户相同
        LambdaQueryWrapper<User> accountQueryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUserAccount, userAccount);
        long userCount = this.count(accountQueryWrapper);
        if (userCount != 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名已存在");
        }
        //注册码不能重复
        LambdaQueryWrapper<User> registerQueryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getRegisterCode, registerCode);
        long registerCodeCount = this.count(registerQueryWrapper);
        if (registerCodeCount != 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册码已被使用");
        }
        //2.插入数据
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //插入
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setRegisterCode(registerCode);
        user.setAvatarUrl("https://baomidou.com/img/logo.svg");
        user.setUserName(RandomStringUtils.randomAlphanumeric(4));
        boolean saveResult = this.save(user);
        if (saveResult != true) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userPassword.contains(" ")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名或密码不能包含空格");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于8位");
        }

        //用户输入账户不能包含特殊字符
        String validPatter = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatter).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名不能包含特殊字符");
        }


        //2.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查看用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, userAccount);
        wrapper.eq(User::getUserPassword, encryptPassword);
        User user = this.getOne(wrapper);
        if (user == null) {
            log.info("Login failed,userAccount cannot match userAccount!");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号与密码不匹配");
        }
        //无效的用户禁止登录
        if(!Objects.equals(user.getUserStatus(), VALID_STATUS)){
            throw new BusinessException(ErrorCode.INVALID_ACCOUNT_ERROR);
        }
        //3.脱敏
        User safetyUser = getSafetyUser(user);

        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);


        return safetyUser;

    }

    /**
     * 用户数据脱敏
     *
     * @param originalUser 原始用户数据
     * @return
     */
    @Override
    public User getSafetyUser(User originalUser) {
        if (originalUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        User safetyUser = new User();
        safetyUser.setId(originalUser.getId());
        safetyUser.setUserRole(originalUser.getUserRole());
        safetyUser.setUserName(originalUser.getUserName());
        safetyUser.setUserAccount(originalUser.getUserAccount());
        safetyUser.setAvatarUrl(originalUser.getAvatarUrl());
        safetyUser.setGender(originalUser.getGender());
        safetyUser.setEmail(originalUser.getEmail());
        safetyUser.setUserStatus(originalUser.getUserStatus());
        safetyUser.setPhone(originalUser.getPhone());
        safetyUser.setCreateTime(originalUser.getCreateTime());
        safetyUser.setRegisterCode(originalUser.getRegisterCode());
        return safetyUser;
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        //移除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }
}




