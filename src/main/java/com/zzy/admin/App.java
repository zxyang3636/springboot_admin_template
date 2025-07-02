package com.zzy.admin;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * packageName com.zzy.admin
 *
 * @author zzy
 * @className com.zzy.admin.App
 * @date 2025/6/30
 * @description TODO
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
