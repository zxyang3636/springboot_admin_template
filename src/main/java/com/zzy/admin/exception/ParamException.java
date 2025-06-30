package com.zzy.admin.exception;

import com.zzy.admin.common.ResultCode;

/**
 * packageName com.zzy.admin.exception
 *
 * @author zzy
 * @className ParamException
 * @date 2025/6/30
 * @description 参数异常类
 */
public class ParamException extends BaseException {
    public ParamException() {
        super(ResultCode.PARAM_ERROR);
    }

    public ParamException(String message) {
        super(ResultCode.PARAM_ERROR.getCode(), message);
    }

    public ParamException(String message, Throwable cause) {
        super(ResultCode.PARAM_ERROR.getCode(), message, cause);
    }
}
