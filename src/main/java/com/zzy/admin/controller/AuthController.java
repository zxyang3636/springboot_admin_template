package com.zzy.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zzy.admin.common.Result;
import com.zzy.admin.annotation.AnonymousAccess;
import com.zzy.admin.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

/**
 * packageName com.zzy.admin.controller
 *
 * @author zzy
 * @className AuthController
 * @date 2025/7/2
 * @description 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    /**
     * 获取公钥
     * 
     * @return 公钥
     */
    @GetMapping("/getPublicKey")
    @AnonymousAccess
    public Result<?> getPublicKey() {
        String publicKey = jwtUtil.getPublicKey();
        return Result.success(publicKey);
    }
}
