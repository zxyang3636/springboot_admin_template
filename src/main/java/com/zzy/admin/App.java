package com.zzy.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * packageName com.zzy.admin
 *
 * @author zzy
 * @className com.zzy.admin.App
 * @date 2025/6/30
 * @description 启动类
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true) // 启用AOP
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
