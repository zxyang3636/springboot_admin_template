package com.zzy.admin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标记可以匿名访问的接口方法
 * 被此注解标记的接口将不会被登录拦截器拦截
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) // 这个注解在运行时依然存在，这样我们才能通过反射读取到它
public @interface AnonymousAccess {

}
