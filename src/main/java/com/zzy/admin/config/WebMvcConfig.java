package com.zzy.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zzy.admin.interceptor.LoginInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer { 
    private final LoginInterceptor loginInterceptor;
    
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/api/auth/login",      // 登录接口
                        "/api/auth/register",   // 注册接口
                        "/api/auth/captcha",    // 验证码接口
                        "/error",               // 错误页面
                        "/favicon.ico",         // 网站图标
                        "/static/**",           // 静态资源
                        "/actuator/**"          // 监控端点
                );
    }
}
