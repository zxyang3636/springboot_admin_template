package com.zzy.admin.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName com.zzy.admin.config
 *
 * @author zzy
 * @className MinioConfig
 * @date 2025/7/7
 * @description TODO
 */
@Configuration
public class MinioConfig {
    @Autowired
    private MinioProp minioProp;

    @Bean
    public MinioClient minioClient() throws Exception {
        return MinioClient.builder().endpoint(minioProp.getEndpoint())
                .credentials(minioProp.getAccesskey(), minioProp.getSecretKey()).build();
    }

}
