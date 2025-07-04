package com.zzy.admin.annotation;

import java.lang.annotation.*;

/**
 * 日志记录注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogRecord {
    /**
     * 操作描述
     */
    String value() default "";
    
    /**
     * 业务类型
     */
    String businessType() default "";
    
    /**
     * 操作人类别
     */
    String operatorType() default "USER";
    
    /**
     * 是否保存请求的参数
     */
    boolean saveRequestData() default true;
    
    /**
     * 是否保存响应的参数
     */
    boolean saveResponseData() default true;
    
    /**
     * 是否记录执行时间
     */
    boolean recordExecuteTime() default true;
}
