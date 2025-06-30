package com.zzy.admin.exception;

import com.zzy.admin.common.ResultCode;

/**
 * packageName com.zzy.admin.exception
 *
 * @author zzy
 * @className PermissionDeniedException
 * @date 2025/6/30
 * @description 权限异常类
 */
public class PermissionDeniedException extends BaseException {
    public PermissionDeniedException() {
        super(ResultCode.PERMISSION_DENIED);
    }

    public PermissionDeniedException(String message) {
        super(ResultCode.PERMISSION_DENIED.getCode(), message);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(ResultCode.PERMISSION_DENIED.getCode(), message, cause);
    }
}
