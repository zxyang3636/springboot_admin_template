package com.zzy.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "spring-admin.jwt")
public class JwtProperties {

    /**
     * 密钥库 (JKS) 文件的位置, Spring Boot 会自动解析 classpath: 或 file:
     */
    private Resource location;
    /**
     * 密钥库中密钥条目的别名
     */
    private String alias;
    /**
     * 密钥库和私钥的密码
     */
    private String password;

    /**
     * JWT 访问令牌过期时间（小时）
     */
    private Integer expiration = 2;

    /**
     * JWT 刷新令牌过期时间（小时）
     */
    private Integer refresh = 168;
}
