package com.zzy.admin.controller;

import com.zzy.admin.common.constant.RedisConstant;
import com.zzy.admin.common.Result;
import com.zzy.admin.common.UserContext;
import com.zzy.admin.common.UserContextHolder;
import com.zzy.admin.domain.dto.RefreshRequest;
import com.zzy.admin.domain.po.SysUser;
import com.zzy.admin.domain.vo.UserVO;
import com.zzy.admin.service.SysUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * packageName com.zzy.admin.controller
 *
 * @author zzy
 * @className UserController
 * @date 2025/6/30
 * @description TODO
 */
@RequestMapping("/user")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final SysUserService sysUserService;


    @PostMapping("/login")
    public Result login(@RequestBody SysUser sysUser) {
        return sysUserService.login(sysUser);
    }

    @GetMapping("/info")
    public Result info() {
        UserVO admin =
                UserVO.builder().avatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                        .username("admin").build();
        return Result.success(admin);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public Result<UserContext> getCurrentUser() {
        UserContext currentUser = UserContextHolder.getContext();
        return Result.success(currentUser);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        UserContext userContext = UserContextHolder.getContext();
        if (userContext != null) {
            redisTemplate.delete(RedisConstant.REFRESH_KEY + userContext.getUserId());
            UserContextHolder.clearContext();
        }
        return Result.success();
    }

    @PostMapping("/refresh")
    public Result<?> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        return sysUserService.refreshToken(refreshRequest);
    }
}
