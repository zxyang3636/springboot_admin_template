package com.zzy.admin.controller;

import com.zzy.admin.common.Result;
import com.zzy.admin.domain.dto.RefreshRequest;
import com.zzy.admin.domain.po.SysUser;
import com.zzy.admin.service.SysUserService;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import com.zzy.admin.annotation.LogRecord;
import com.zzy.admin.annotation.AnonymousAccess;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * packageName com.zzy.admin.controller
 *
 * @author zzy
 * @className UserController
 * @date 2025/6/30
 * @description 用户控制器
 */
@RequestMapping("/user")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;

    /**
     * 登录
     */
    @PostMapping("/login")
    @LogRecord(value = "登录", businessType = "认证")
    @AnonymousAccess
    public Result<?> login(@RequestBody SysUser sysUser) {
        return sysUserService.login(sysUser);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<?> info() {
        return sysUserService.info();
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @AnonymousAccess
    @LogRecord(value = "退出登录", businessType = "退出登录")
    public Result<Void> logout(HttpServletRequest request) {
        return sysUserService.logout(request);
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    @AnonymousAccess
    @LogRecord(value = "刷新令牌", businessType = "认证")
    public Result<?> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        return sysUserService.refreshToken(refreshRequest);
    }

    /**
     * 测试
     */
    @GetMapping("/test")
    @LogRecord(value = "测试", businessType = "测试")
    public Result<?> test() {
        return Result.success("test续签token");
    }
}
