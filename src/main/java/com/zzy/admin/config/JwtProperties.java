package com.zzy.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * JWT 签名密钥
     */
    private String secret = "gP#73YvUq!4rEwzX1@cN9kDf";

    /**
     * JWT 访问令牌过期时间（小时）
     */
    private Integer expiration = 2;

    /**
     * JWT 刷新令牌过期时间（小时）
     */
    private Integer refresh = 168;

    /**
     * JWT 令牌前缀
     */
    private String tokenPrefix = "Bearer ";

    /**
     * JWT 令牌请求头名称
     */
    private String header = "Authorization";

    private String salt = "UoL@f8YP#rKWZ9XtE3bGjMCd76qvN1As";
}
