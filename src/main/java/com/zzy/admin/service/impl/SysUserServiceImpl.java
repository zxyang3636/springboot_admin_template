package com.zzy.admin.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.crypto.SecureUtil;
import com.zzy.admin.common.constant.RedisConstant;
import com.zzy.admin.common.Result;
import com.zzy.admin.common.UserContext;
import com.zzy.admin.common.UserContextHolder;
import com.zzy.admin.domain.dto.RefreshRequest;
import com.zzy.admin.domain.vo.UserVO;
import com.zzy.admin.exception.BusinessException;
import com.zzy.admin.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zzy.admin.domain.po.SysUser;
import com.zzy.admin.mapper.SysUserMapper;
import com.zzy.admin.service.SysUserService;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Value("${jwt.salt}")
    private String salt;

    @Value("${jwt.refresh}")
    private Long hour;

    private final JwtUtil jwtUtil;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public int batchInsert(List<SysUser> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public Result<?> login(SysUser sysUser) {
        String username = sysUser.getUsername();
        String password = SecureUtil.md5(sysUser.getPassword() + salt);
        SysUser user = queryUser(username);
        if (ObjUtil.isEmpty(user)) {
            throw new BusinessException("用户不存在");
        }
        if (user.getStatus().equals(1)) {
            throw new BusinessException("账号已被禁用！");
        }
        String dbPassword = user.getPassword();
        if (!password.equals(dbPassword)) {
            throw new BusinessException("账号或密码错误");
        }
        // 签发
        String accessToken = jwtUtil.generateAccessToken(user.getId(), username);
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), username);
        redisTemplate.opsForValue().set(RedisConstant.REFRESH_KEY + user.getId(), refreshToken, hour, TimeUnit.HOURS);
        // 保存用户上下文
        buildUserContext(user);
        UserVO userVO = buildUserVO(user, accessToken, refreshToken);
        return Result.success("登录成功", userVO);
    }

    @Override
    public Result<?> refreshToken(RefreshRequest request) {
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
            String storedRefreshToken = (String) redisTemplate.opsForValue().get(RedisConstant.REFRESH_KEY + userId);
            if (!refreshToken.equals(storedRefreshToken)) {
                return Result.fail(401, "刷新令牌已失效");
            }

            // 4. 从数据库重新获取用户信息（确保用户状态正常）
            SysUser user = getById(userId);
            if (user == null || user.getStatus() != 0) {
                return Result.fail(401, "用户状态异常");
            }

            // 5. 生成新的访问令牌
            String newAccessToken = jwtUtil.generateAccessToken(userId, username);

            // 6. 生成新的刷新令牌（可选，提高安全性）
            String newRefreshToken = jwtUtil.generateRefreshToken(userId, username);

            // 7. 更新Redis中的刷新令牌
            redisTemplate.opsForValue().set(RedisConstant.REFRESH_KEY + userId, newRefreshToken, hour, TimeUnit.HOURS);

            // 8. 返回新令牌
            UserVO userVO = buildUserVO(user, newAccessToken, newRefreshToken);
            return Result.success(userVO);

        } catch (Exception e) {
            log.error("刷新令牌失败", e);
            return Result.fail(401, "刷新令牌失败");
        }
    }

    private UserVO buildUserVO(SysUser user, String accessToken, String refreshToken) {
        return UserVO.builder().accessToken(accessToken)
                .refreshToken(refreshToken)
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }

    private void buildUserContext(SysUser user) {
        UserContext userContext = new UserContext();
        userContext.setUserId(user.getId());
        userContext.setUsername(user.getUsername());
        userContext.setNickname(user.getNickname());
        userContext.setEmail(user.getEmail());
        userContext.setRole(null);
        userContext.setAvatar(user.getAvatar());
        userContext.setLoginTime(LocalDateTime.now());
        userContext.setLoginIp(null);
        // 保存用户上下文
        UserContextHolder.setContext(userContext);
    }

    public SysUser queryUser(String username) {
        return lambdaQuery()
                .eq(SysUser::getUsername, username)
                .one();
    }
}
