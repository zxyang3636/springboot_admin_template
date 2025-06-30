package com.zzy.admin.exception;

import com.zzy.admin.common.ResultCode;

/**
 * packageName com.zzy.admin.exception
 *
 * @author zzy
 * @className AuthException
 * @date 2025/6/30
 * @description 认证异常类
 */
public class AuthException extends BaseException {
    public AuthException() {
        super(ResultCode.UNAUTHORIZED);
    }

    public AuthException(String message) {
        super(ResultCode.UNAUTHORIZED.getCode(), message);
    }

    public AuthException(ResultCode resultCode) {
        super(resultCode);
    }

    public AuthException(String message, Throwable cause) {
        super(ResultCode.UNAUTHORIZED.getCode(), message, cause);
    }
}
