package com.zzy.admin.exception;

/**
 * packageName com.zzy.admin.exception
 *
 * @author zzy
 * @className GlobalExceptionHandler
 * @date 2025/6/30
 * @description TODO
 */

import com.zzy.admin.common.Result;
import com.zzy.admin.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 基础异常处理
     */
    @ExceptionHandler(BaseException.class)
    public Result<Void> handleBaseException(BaseException e, HttpServletRequest request) {
        log.error("业务异常：{}, 请求路径：{}", e.getMessage(), request.getRequestURI(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常：{}, 请求路径：{}", e.getMessage(), request.getRequestURI());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数异常处理
     */
    @ExceptionHandler(ParamException.class)
    public Result<Void> handleParamException(ParamException e, HttpServletRequest request) {
        log.warn("参数异常：{}, 请求路径：{}", e.getMessage(), request.getRequestURI());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 权限异常处理
     */
    @ExceptionHandler(PermissionDeniedException.class)
    public Result<Void> handlePermissionDeniedException(PermissionDeniedException e, HttpServletRequest request) {
        log.warn("权限异常：{}, 请求路径：{}", e.getMessage(), request.getRequestURI());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 认证异常处理
     */
    @ExceptionHandler(AuthException.class)
    public Result<Void> handleAuthException(AuthException e, HttpServletRequest request) {
        log.warn("认证异常：{}, 请求路径：{}", e.getMessage(), request.getRequestURI());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常处理（@RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errorMsg = new StringBuilder();

        for (FieldError fieldError : fieldErrors) {
            errorMsg.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }

        log.warn("参数校验异常：{}", errorMsg.toString());
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), errorMsg.toString());
    }

    /**
     * 参数绑定异常处理（@ModelAttribute）
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errorMsg = new StringBuilder();

        for (FieldError fieldError : fieldErrors) {
            errorMsg.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }

        log.warn("参数绑定异常：{}", errorMsg.toString());
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), errorMsg.toString());
    }

    /**
     * 非法参数异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数异常：{}", e.getMessage());
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 系统异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常：{}, 请求路径：{}", e.getMessage(), request.getRequestURI(), e);
        return Result.fail(ResultCode.SYSTEM_ERROR);
    }
}
