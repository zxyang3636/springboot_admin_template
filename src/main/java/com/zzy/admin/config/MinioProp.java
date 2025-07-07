package com.zzy.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * packageName com.zzy.admin.config
 *
 * @author zzy
 * @className MinioProp
 * @date 2025/7/7
 * @description TODO
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProp {
    private String endpoint;
    private String accesskey;
    private String secretKey;
}
