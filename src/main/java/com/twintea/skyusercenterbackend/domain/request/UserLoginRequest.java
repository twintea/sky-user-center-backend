package com.twintea.skyusercenterbackend.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -6034325134234472164L;
    private String userAccount;
    private String userPassword;
}
