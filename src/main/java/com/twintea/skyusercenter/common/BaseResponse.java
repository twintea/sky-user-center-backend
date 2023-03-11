package com.twintea.skyusercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -27627570252259361L;

    private int code;

    private T data;

    private String msg;

    private String description;

    public BaseResponse(int code, T data, String msg,String description) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.description = description;
    }
    public BaseResponse(int code, T data, String msg){
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
    public BaseResponse(int code, T data) {
        this(code, data, "","");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMsg(),errorCode.getDescription());
    }



}
