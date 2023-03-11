package com.twintea.skyusercenter.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", "请求参数错误"),
    NULL_ERROR(40001, "请求数据为空", "请求数据为空"),
    NO_LOGIN(40100, "未登录", "未登录"),
    NO_AUTH(40100, "无权限", "无权限"),
    SYSTEM_ERROR(50000, "系统内部异常", "系统内部异常"),
    INVALID_ACCOUNT_ERROR(40300, "账号异常，请联系管理员", "账号异常，请联系管理员");


    private final int code;
    private final String msg;
    private final String description;

    ErrorCode(int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }
}
