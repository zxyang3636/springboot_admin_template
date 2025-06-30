package com.zzy.admin.common;

/**
 * packageName com.zzy.admin.common
 *
 * @author zzy
 * @className ResultCode
 * @date 2025/6/30
 * @description TODO
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结果码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    // ========== 通用状态码 ==========
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统异常"),
    PARAM_ERROR(400, "参数错误"),

    // ========== 认证授权 ==========
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    TOKEN_EXPIRED(4001, "Token已过期"),
    TOKEN_INVALID(4002, "Token无效"),

    // ========== 业务错误 ==========
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    ACCOUNT_DISABLED(1004, "账号已禁用"),

    ROLE_NOT_FOUND(2001, "角色不存在"),
    ROLE_ALREADY_EXISTS(2002, "角色已存在"),
    ROLE_IN_USE(2003, "角色正在使用中"),

    PERMISSION_DENIED(3001, "权限不足"),
    RESOURCE_NOT_FOUND(3002, "资源不存在"),

    // ========== 数据操作 ==========
    DATA_NOT_FOUND(4001, "数据不存在"),
    DATA_ALREADY_EXISTS(4002, "数据已存在"),
    DATA_IN_USE(4003, "数据正在使用中"),
    DELETE_FAILED(4004, "删除失败"),
    UPDATE_FAILED(4005, "更新失败"),

    // ========== 文件操作 ==========
    FILE_UPLOAD_FAILED(5001, "文件上传失败"),
    FILE_TYPE_ERROR(5002, "文件类型错误"),
    FILE_SIZE_EXCEEDED(5003, "文件大小超限"),
    FILE_NOT_FOUND(5004, "文件不存在");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 消息
     */
    private final String message;
}
