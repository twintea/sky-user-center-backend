package com.twintea.skyusercenter.common;

/**
 * 返回结果工具类
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"ok","");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static  BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }
    public static  BaseResponse error(int code,String msg,String descpriton){
        return new BaseResponse(code,null,msg,descpriton);
    }

    public static  BaseResponse error(ErrorCode errorCode,String msg,String descpriton){
        return new BaseResponse(errorCode.getCode(),null,msg,descpriton);
    }
    public static  BaseResponse error(ErrorCode errorCode,String descpriton){
        return new BaseResponse(errorCode.getCode(),null,errorCode.getMsg(),descpriton);
    }
}
