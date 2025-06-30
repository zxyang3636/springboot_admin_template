package com.zzy.admin.exception;

import com.zzy.admin.common.ResultCode;

/**
 * packageName com.zzy.admin.exception
 *
 * @author zzy
 * @className BusinessException
 * @date 2025/6/30
 * @description 业务异常类
 */
public class BusinessException extends BaseException {
    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
