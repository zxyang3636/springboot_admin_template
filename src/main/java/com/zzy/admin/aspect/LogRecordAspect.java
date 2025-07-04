package com.zzy.admin.aspect;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.zzy.admin.annotation.LogRecord;
import com.zzy.admin.domain.po.OperationLog;
import com.zzy.admin.utils.JwtUtil;
import com.zzy.admin.utils.LogUtils;
import com.zzy.admin.common.UserContextHolder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LogRecordAspect {

    private final JwtUtil jwtUtil;

    @Around("@annotation(logRecord)")
    public Object around(ProceedingJoinPoint joinPoint, LogRecord logRecord) throws Throwable {
        // 开始时间
        long startTime = System.currentTimeMillis();

        // 创建操作日志对象
        OperationLog operationLog = new OperationLog();

        // 提前获取用户信息
        String operatorName = extractOperatorName(joinPoint);

        try {
            // 设置基本信息
            setBasicInfo(joinPoint, logRecord, operationLog);
            if (operatorName != null) {
                operationLog.setOperatorName(operatorName);
            }

            // 记录请求参数
            if (logRecord.saveRequestData()) {
                String requestParam = LogUtils.argsToString(joinPoint.getArgs());
                operationLog.setRequestParam(requestParam);
            }

            // 记录开始日志
            logStart(operationLog);

            // 执行目标方法
            Object result = joinPoint.proceed();

            // 记录返回结果
            if (logRecord.saveResponseData()) {
                String responseResult = LogUtils.resultToString(result);
                operationLog.setResponseResult(responseResult);
            }

            // 设置成功状态
            operationLog.setStatus("SUCCESS");

            return result;

        } catch (Throwable e) {
            // 设置失败状态
            operationLog.setStatus("FAILED");
            operationLog.setErrorMsg(LogUtils.formatException(e));

            // 记录错误日志
            logError(operationLog, e);

            throw e;

        } finally {
            // 计算执行时间
            if (logRecord.recordExecuteTime()) {
                long executeTime = System.currentTimeMillis() - startTime;
                operationLog.setExecuteTime(executeTime);
            }

            // 记录完成日志
            logComplete(operationLog);
        }
    }

    /**
     * 设置基本信息
     */
    private void setBasicInfo(ProceedingJoinPoint joinPoint, LogRecord logRecord, OperationLog operationLog) {
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();

        // 设置操作信息
        operationLog.setOperation(logRecord.value())
                .setBusinessType(logRecord.businessType())
                .setOperatorType(logRecord.operatorType())
                .setMethod(className + "." + methodName)
                .setOperateTime(LocalDateTime.now())
                .setRequestUrl(LogUtils.getRequestUrl())
                .setRequestMethod(LogUtils.getRequestMethod())
                .setOperatorIp(LogUtils.getClientIp());

        // 设置操作人信息
        if (UserContextHolder.isLogin()) {
            operationLog.setOperatorId(UserContextHolder.getCurrentUserId())
                    .setOperatorName(UserContextHolder.getCurrentUsername());
        }
    }

    /**
     * 记录开始日志
     */
    private void logStart(OperationLog operationLog) {
        log.info("=== 操作开始 === | 操作: {} | 业务类型: {} | 操作人: {} | 方法: {} | 参数: {}",
                operationLog.getOperation(),
                operationLog.getBusinessType(),
                operationLog.getOperatorName(),
                operationLog.getMethod(),
                operationLog.getRequestParam());
    }

    /**
     * 记录错误日志
     */
    private void logError(OperationLog operationLog, Throwable e) {
        log.error("=== 操作异常 === | 操作: {} | 业务类型: {} | 操作人: {} | 方法: {} | 错误: {}",
                operationLog.getOperation(),
                operationLog.getBusinessType(),
                operationLog.getOperatorName(),
                operationLog.getMethod(),
                operationLog.getErrorMsg(),
                e);
    }

    /**
     * 记录完成日志
     */
    private void logComplete(OperationLog operationLog) {
        if ("SUCCESS".equals(operationLog.getStatus())) {
            log.info("=== 操作完成 === | 操作: {} | 状态: {} | 执行时间: {}ms | 操作人: {} | 结果: {}",
                    operationLog.getOperation(),
                    operationLog.getStatus(),
                    operationLog.getExecuteTime(),
                    operationLog.getOperatorName(),
                    operationLog.getResponseResult());
        } else {
            log.warn("=== 操作失败 === | 操作: {} | 状态: {} | 执行时间: {}ms | 操作人: {} | 错误: {}",
                    operationLog.getOperation(),
                    operationLog.getStatus(),
                    operationLog.getExecuteTime(),
                    operationLog.getOperatorName(),
                    operationLog.getErrorMsg());
        }

        // 如果需要持久化到数据库，可以在这里调用保存方法
        // saveOperationLog(operationLog);
    }


      /**
     * 从请求中提取用户信息
     */
    private String extractOperatorName(ProceedingJoinPoint joinPoint) {
        // 1. 首先尝试从 UserContext 获取
        if (UserContextHolder.isLogin()) {
            return UserContextHolder.getCurrentUsername();
        }
        
        // 2. 如果 UserContext 为空，尝试从请求中获取 token
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null) {
                for (Object arg : args) {
                    if (arg instanceof HttpServletRequest) {
                        HttpServletRequest request = (HttpServletRequest) arg;
                        String token = extractToken(request);
                        if (token != null) {
                            // 即使 token 过期也尝试解析用户信息
                            return jwtUtil.getUsernameFromToken(token);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.debug("从token中提取用户名失败", e);
        }
        
        return null;
    }
    
    /**
     * 从请求中提取令牌
     */
    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }
}
