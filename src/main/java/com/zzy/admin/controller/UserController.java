package com.zzy.admin.controller;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.zzy.admin.common.Result;
import com.zzy.admin.common.UserContext;
import com.zzy.admin.common.UserContextHolder;
import com.zzy.admin.domain.vo.UserVO;
import com.zzy.admin.exception.BusinessException;
import com.zzy.admin.exception.ParamException;
import com.zzy.admin.service.impl.UserContextService;
import com.zzy.admin.utils.JwtUtil;

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

    // @PostMapping("/login")
    // public Result login(@RequestBody JSONObject jsonObject) {
    // UserVO adminToken = UserVO.builder().token("Admin Token").build();
    // String username = jsonObject.getString("username");
    // String password = jsonObject.getString("password");
    // if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
    // throw new ParamException();
    // }
    // if (username.equals("admin") && password.equals("111111")) {
    // return Result.success(adminToken);
    // }
    // throw new BusinessException("账号或密码错误");
    // }

    // @GetMapping("/info")
    // public Result info() {
    // UserVO admin =
    // UserVO.builder().avatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
    // .username("admin").build();
    // return Result.success(admin);
    // }

    private final UserContextService userContextService;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 登录接口示例
     */
    @PostMapping("/login")
    public Result<String> login(@RequestParam String username, @RequestParam String password) {
        // TODO: 验证用户名密码（这里只是示例）

        // 生成访问令牌
        String accessToken = jwtUtil.generateToken(1L, username);
        // 生成刷新令牌
        String refreshToken = jwtUtil.generateRefreshToken(1L, username);

        redisTemplate.opsForValue().set("refresh:" + 1L, refreshToken, 7, TimeUnit.DAYS);

        // 创建用户上下文
        UserContext userContext = new UserContext();
        userContext.setUserId(1L);
        userContext.setUsername(username);
        userContext.setNickname("管理员");
        userContext.setEmail("admin@example.com");
        userContext.setRole("ADMIN");
        userContext.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        userContext.setLoginTime(LocalDateTime.now());
        userContext.setLoginIp("127.0.0.1");

        // 保存用户上下文
        userContextService.saveUserContext(accessToken, userContext);

        log.info("用户登录成功，用户名: {}, 令牌: {}", username, accessToken);
        return Result.success(accessToken);
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
            redisTemplate.delete("refresh:" + userContext.getUserId());
            userContextService.removeUserContext(userContext.getToken());
        }
        return Result.success();
    }

    @PostMapping("/refresh")
    public Result<?> refreshToken(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        try {
            // 1. 验证刷新令牌格式是否正确
            if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
                return Result.fail(401, "刷新令牌无效");
            }

            // 2. 从刷新令牌中获取用户信息
            Long userId = jwtUtil.getUserIdFromToken(refreshToken);
            String username = jwtUtil.getUsernameFromToken(refreshToken);

            // 3. 检查Redis中是否存在这个刷新令牌（防止令牌被盗用）
            String storedRefreshToken = (String) redisTemplate.opsForValue().get("refresh:" + userId);
            if (!refreshToken.equals(storedRefreshToken)) {
                return Result.fail(401, "刷新令牌已失效");
            }

            // 4. 从数据库重新获取用户信息（确保用户状态正常）
            // User user = userService.getById(userId);
            // if (user == null || user.getStatus() != 1) {
            // return Result.error(401, "用户状态异常");
            // }

            // 5. 生成新的访问令牌
            String newAccessToken = jwtUtil.generateToken(1L, username);

            // 6. 生成新的刷新令牌（可选，提高安全性）
            String newRefreshToken = jwtUtil.generateRefreshToken(userId, username);

            // 7. 更新Redis中的刷新令牌
            redisTemplate.opsForValue().set("refresh:" + userId, newRefreshToken, 7, TimeUnit.DAYS);

            // 8. 返回新令牌
            // LoginResponse response = new LoginResponse();
            // response.setAccessToken(newAccessToken);
            // response.setRefreshToken(newRefreshToken);
            // response.setExpiresIn(3600L);

            log.info("刷新令牌成功，用户ID: {}", userId);
            return Result.success(newAccessToken);

        } catch (Exception e) {
            log.error("刷新令牌失败", e);
            return Result.fail(401, "刷新令牌失败");
        }
    }
}
