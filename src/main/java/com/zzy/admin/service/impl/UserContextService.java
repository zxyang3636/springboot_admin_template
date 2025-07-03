package com.zzy.admin.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;
import com.zzy.admin.common.UserContext;
import com.zzy.admin.exception.AuthException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserContextService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis中用户上下文的键前缀
     */
    private static final String USER_CONTEXT_PREFIX = "user:context:";

    /**
     * 默认会话超时时间（30分钟）
     */
    private static final long DEFAULT_TIMEOUT = 30;

    // /**
    //  * 保存用户上下文到Redis
    //  * 
    //  * @param token          会话令牌
    //  * @param userContext    用户上下文信息
    //  * @param timeoutMinutes 超时时间（分钟），如果为null则使用默认超时时间
    //  */
    // public void saveUserContext(String token, UserContext userContext, Long timeoutMinutes) {

    //     if (userContext == null) {
    //         throw new AuthException("用户上下文信息不能为空");
    //     }

    //     try {
    //         String key = buildContextKey(token);

    //     } catch (Exception e) {
    //         log.error("保存用户上下文失败，令牌: {}", token, e);
    //         throw new AuthException("保存用户会话失败");
    //     }
    // }

    // /**
    //  * 保存用户上下文到Redis（使用默认超时时间）
    //  * 
    //  * @param token       会话令牌
    //  * @param userContext 用户上下文信息
    //  */
    // public void saveUserContext(String token, UserContext userContext) {
    //     saveUserContext(token, userContext, null);
    // }

    /**
     * 根据令牌获取用户上下文
     * 
     * @param token 会话令牌
     * @return 用户上下文信息，如果不存在或已过期返回null
     */
    public UserContext getUserContext(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }

        try {
            String key = buildContextKey(token);
            Object obj = redisTemplate.opsForValue().get(key);

            if (obj instanceof UserContext) {
                UserContext userContext = (UserContext) obj;
                return userContext;
            }

            log.debug("用户上下文不存在或已过期，令牌: {}", token);
            return null;

        } catch (Exception e) {
            log.error("获取用户上下文失败，令牌: {}", token, e);
            return null;
        }
    }

    /**
     * 刷新用户上下文过期时间
     * 
     * @param token          会话令牌
     * @param timeoutMinutes 新的超时时间（分钟）
     * @return true-刷新成功，false-刷新失败
     */
    public boolean refreshUserContext(String token, Long timeoutMinutes) {
        if (StrUtil.isBlank(token)) {
            return false;
        }

        try {
            String key = buildContextKey(token);
            long timeout = timeoutMinutes != null ? timeoutMinutes : DEFAULT_TIMEOUT;

            Boolean result = redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
            if (Boolean.TRUE.equals(result)) {
                log.debug("刷新用户上下文过期时间成功，令牌: {}, 新超时时间: {}分钟", token, timeout);
                return true;
            }

            log.warn("刷新用户上下文过期时间失败，可能令牌不存在，令牌: {}", token);
            return false;

        } catch (Exception e) {
            log.error("刷新用户上下文过期时间异常，令牌: {}", token, e);
            return false;
        }
    }



    /**
     * 检查令牌是否有效
     * 
     * @param token 会话令牌
     * @return true-有效，false-无效
     */
    public boolean isTokenValid(String token) {
        return getUserContext(token) != null;
    }

    /**
     * 构建Redis中用户上下文的键
     * 
     * @param token 会话令牌
     * @return Redis键
     */
    private String buildContextKey(String token) {
        return USER_CONTEXT_PREFIX + token;
    }

}
