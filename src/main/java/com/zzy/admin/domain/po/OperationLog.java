package com.zzy.admin.domain.po;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 操作日志实体
 */
@Data
@Accessors(chain = true)
public class OperationLog {
      /**
     * 操作描述
     */
    private String operation;
    
    /**
     * 业务类型
     */
    private String businessType;
    
    /**
     * 操作人ID
     */
    private Long operatorId;
    
    /**
     * 操作人用户名
     */
    private String operatorName;
    
    /**
     * 操作人类别
     */
    private String operatorType;
    
    /**
     * 请求方法名
     */
    private String method;
    
    /**
     * 请求参数
     */
    private String requestParam;
    
    /**
     * 返回结果
     */
    private String responseResult;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executeTime;
    
    /**
     * 操作状态 SUCCESS/FAILED
     */
    private String status;
    
    /**
     * 错误信息
     */
    private String errorMsg;
    
    /**
     * 操作IP
     */
    private String operatorIp;
    
    /**
     * 操作时间
     */
    private LocalDateTime operateTime;
    
    /**
     * 请求URL
     */
    private String requestUrl;
    
    /**
     * 请求方式
     */
    private String requestMethod;
}
