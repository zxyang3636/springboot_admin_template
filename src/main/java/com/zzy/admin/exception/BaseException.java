package com.zzy.admin.exception;

/**
 * packageName com.zzy.admin.exception
 *
 * @author zzy
 * @className BaseException
 * @date 2025/6/30
 * @description TODO
 */

import com.zzy.admin.common.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
        this.code = ResultCode.SYSTEM_ERROR.getCode();
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = ResultCode.SYSTEM_ERROR.getCode();
    }

    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
