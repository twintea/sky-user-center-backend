package com.twintea.skyusercenter.exception;

import com.twintea.skyusercenter.common.BaseResponse;
import com.twintea.skyusercenter.common.ErrorCode;
import com.twintea.skyusercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException: "+e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(), e.getDescriptionn());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeException(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"系统内部错误");
    }
}
