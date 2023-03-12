package com.twintea.skyusercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.twintea.skyusercenterbackend.common.BaseResponse;
import com.twintea.skyusercenterbackend.common.ErrorCode;
import com.twintea.skyusercenterbackend.common.ResultUtils;
import com.twintea.skyusercenterbackend.domain.User;
import com.twintea.skyusercenterbackend.domain.request.UserLoginRequest;
import com.twintea.skyusercenterbackend.domain.request.UserRegisterRequest;
import com.twintea.skyusercenterbackend.exception.BusinessException;
import com.twintea.skyusercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.twintea.skyusercenterbackend.constant.UserConstant.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
           throw new  BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String registerCode = userRegisterRequest.getRegisterCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,registerCode)) {
             throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, registerCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        Boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        /*用户的数据可能会发生改变，所以不能直接返回从session取到的user
        1. 可以利用缓存更新更新用户的数据，从缓存中去取
        2.直接查库（这里选用这种方式
         */
        if (user == null){
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        User currentUser = userService.getById(user.getId());
        if (!Objects.equals(currentUser.getUserStatus(), VALID_STATUS)){
            throw new BusinessException(ErrorCode.INVALID_ACCOUNT_ERROR);
        }
        User currentSafetyUser = userService.getSafetyUser(currentUser);
        return ResultUtils.success(currentSafetyUser);
    }

    @GetMapping("/search")
    //鉴权
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            userLambdaQueryWrapper.like(User::getUserName, username);
        }
        // todo 不应该把自己展示出来
        List<User> users = userService.list(userLambdaQueryWrapper);
        users.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(users);
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }


    /**
     * 是否为管理员
     *
     * @param request
     * @return 是或者不是管理员
     */
    private Boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null || !Objects.equals(user.getUserRole(), ADMIN_ROLE)) {
            return false;
        }
        return true;
    }
}
