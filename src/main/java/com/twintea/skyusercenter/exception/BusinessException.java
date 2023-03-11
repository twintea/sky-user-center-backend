package com.twintea.skyusercenter.exception;

import com.twintea.skyusercenter.common.ErrorCode;
import lombok.Getter;

/**
 * 自定义全局异常处理
 */
@Getter
public class BusinessException extends RuntimeException{

    private final int code;
    private final String descriptionn;

    public BusinessException(String message, int code, String descriptionn) {
        super(message);
        this.code = code;
        this.descriptionn = descriptionn;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.descriptionn = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode,String descriptionn) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.descriptionn = descriptionn;
    }
}
