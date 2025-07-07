package com.zzy.admin.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.crypto.SecureUtil;
import com.zzy.admin.common.constant.RedisConstant;
import com.zzy.admin.common.Result;
import com.zzy.admin.domain.dto.RefreshRequest;
import com.zzy.admin.domain.vo.UserVO;
import com.zzy.admin.exception.BusinessException;
import com.zzy.admin.utils.CryptoUtils;
import com.zzy.admin.utils.JwtUtil;
import com.zzy.admin.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.admin.domain.po.SysUser;
import com.zzy.admin.mapper.SysUserMapper;
import com.zzy.admin.service.SysUserService;
import com.zzy.admin.common.UserContextHolder;
import com.zzy.admin.common.UserContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Value("${spring-admin.security.salt}")
    private String salt;

    @Value("${spring-admin.jwt.refresh}")
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
        String password = sysUser.getPassword();
        String aesKey = sysUser.getAesKey();
        String iv = sysUser.getDecodeIv();
        // 解密密码
        String decryptPassword = decryptPassword(password, aesKey, iv);
        // password = SecureUtil.md5(decryptPassword + salt);
//        password = PasswordUtils.encode(decryptPassword);
        SysUser user = queryUser(username);
        if (ObjUtil.isEmpty(user)) {
            throw new BusinessException("用户不存在");
        }
        if (user.getStatus().equals(1)) {
            throw new BusinessException("账号已被禁用！");
        }
        String dbPassword = user.getPassword();
        if (!PasswordUtils.matches(decryptPassword, dbPassword)) {
            throw new BusinessException("账号或密码错误");
        }
        // if (!password.equals(dbPassword)) {
        //     throw new BusinessException("账号或密码错误");
        // }
        // 签发
        String accessToken = jwtUtil.generateAccessToken(user.getId(), username, user.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), username, user.getNickname());
        redisTemplate.opsForValue().set(RedisConstant.REFRESH_KEY + user.getId(), refreshToken, hour, TimeUnit.HOURS);

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
            String nickname = jwtUtil.getNicknameFromToken(refreshToken);
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
            String newAccessToken = jwtUtil.generateAccessToken(userId, username, nickname);

            // 6. 返回新令牌
            UserVO userVO = buildUserVO(user, newAccessToken, refreshToken);
            return Result.success(userVO);

        } catch (Exception e) {
            log.error("刷新令牌失败", e);
            return Result.fail(401, "刷新令牌失败");
        }
    }

    @Override
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
                // log.info("用户退出登录成功，用户ID: {}", userId);
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

    @Override
    public Result<?> info() {
        Long userId = UserContextHolder.getCurrentUserId();
        SysUser sysUser = getById(userId);
        UserVO userVO = UserVO.builder()
                .avatar(sysUser.getAvatar())
                .username(sysUser.getUsername())
                .nickname(sysUser.getNickname())
                .build();
        return Result.success(userVO);
    }

    private UserVO buildUserVO(SysUser user, String accessToken, String refreshToken) {
        return UserVO.builder().accessToken(accessToken)
                .refreshToken(refreshToken)
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }

    public SysUser queryUser(String username) {
        return lambdaQuery()
                .eq(SysUser::getUsername, username)
                .one();
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

    /**
     * 解密密码
     * 
     * @param password aes加密后的密码
     * @param aesKey   加密+base64编码后的aesKey
     * @param iv       base64编码后的iv
     * @return 解密后的密码
     */
    private String decryptPassword(String password, String aesKey, String iv) {
        // 私钥解密aesKey
        String aes = null;
        try {
            String privateKey = jwtUtil.getPrivateKey();
            PrivateKey privateKeyObj = CryptoUtils.stringToPrivateKey(privateKey);
            aes = CryptoUtils.rsaDecrypt(aesKey, privateKeyObj);
        } catch (Exception e) {
            log.error("aesKey解密失败", e);
            throw new BusinessException("aesKey解密失败");
        }
        try {
            // aes解密password
            SecretKey resultAesKey = CryptoUtils.stringToAesKey(aes);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            String decryptPassword = CryptoUtils.aesDecrypt(password, resultAesKey, new IvParameterSpec(ivBytes));
            return decryptPassword;
        } catch (Exception e) {
            log.error("password解密失败", e);
            throw new BusinessException("password解密失败");
        }
    }
}
