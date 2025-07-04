package com.zzy.admin.controller;

import com.zzy.admin.common.constant.RedisConstant;
import com.zzy.admin.common.Result;
import com.zzy.admin.common.UserContext;
import com.zzy.admin.common.UserContextHolder;
import com.zzy.admin.domain.dto.RefreshRequest;
import com.zzy.admin.domain.po.SysUser;
import com.zzy.admin.domain.vo.UserVO;
import com.zzy.admin.service.SysUserService;
import com.zzy.admin.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import com.zzy.admin.annotation.LogRecord;

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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final SysUserService sysUserService;
    private final JwtUtil jwtUtil;


    @PostMapping("/login")
    @LogRecord(value = "登录", businessType = "认证")
    public Result<?> login(@RequestBody SysUser sysUser) {
        return sysUserService.login(sysUser);
    }

    @GetMapping("/info")
    @LogRecord(value = "获取用户信息", businessType = "获取用户信息")
    public Result<?> info() {
        UserContext userContext = UserContextHolder.getContext();
        UserVO userVO = UserVO.builder()
        .avatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                .username(userContext.getUsername())
                .nickname(userContext.getNickname())
                .build();
        return Result.success(userVO);
    }


    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @LogRecord(value = "退出登录", businessType = "退出登录")
    public Result<Void> logout(HttpServletRequest request) {
        try {
            // 尝试从ThreadLocal获取用户上下文（如果token有效）
            UserContext userContext = UserContextHolder.getContext();
            Long userId = null;
            
            if (userContext != null) {
                // token有效的情况
                userId = userContext.getUserId();
            } else {
                // token无效的情况，手动解析token获取用户ID
                String token = extractToken(request);
                if (token != null) {
                    try {
                        // 尝试解析token，即使过期也要获取用户ID
                        userId = jwtUtil.getUserIdFromToken(token);
                    } catch (Exception e) {
                        log.warn("退出登录时token解析失败: {}", e.getMessage());
                    }
                }
            }
            
            // 如果获取到了用户ID，清理相关数据
            if (userId != null) {
                // 删除刷新令牌
                redisTemplate.delete(RedisConstant.REFRESH_KEY + userId);
                log.info("用户退出登录成功，用户ID: {}", userId);
            } else {
                log.info("用户退出登录，但无法获取用户ID");
            }
            
            // 清除ThreadLocal（如果有的话）
            UserContextHolder.clearContext();
            
            return Result.success();
            
        } catch (Exception e) {
            log.error("退出登录处理异常", e);
            // 即使出现异常，也返回成功，因为退出登录应该总是成功的
            return Result.success();
        }
    }
    
    /**
     * 从请求中提取令牌
     */
    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    @PostMapping("/refresh")
    @LogRecord(value = "刷新令牌", businessType = "认证")
    public Result<?> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        return sysUserService.refreshToken(refreshRequest);
    }

    @GetMapping("/test")
    public Result<?> test() {
        return Result.success("test续签token");
    }
}
