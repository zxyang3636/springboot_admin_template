package com.zzy.admin.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.zzy.admin.common.UserContext;
import com.zzy.admin.common.UserContextHolder;
import com.zzy.admin.exception.AuthException;
import com.zzy.admin.service.impl.UserContextService;
import com.zzy.admin.utils.JwtUtil;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor    
public class LoginInterceptor implements HandlerInterceptor {

    private final UserContextService userContextService;

    private final JwtUtil jwtUtil;
    
    /**
     * 请求头中令牌的键名
     */
    private static final String TOKEN_HEADER = "Authorization";
    
    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";
    
    /**
     * 在请求处理之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 获取令牌
            String token = extractToken(request);
            if (StrUtil.isBlank(token)) {
                throw new AuthException();
            }

            jwtUtil.validateToken(request.getHeader(TOKEN_HEADER));

            // 获取用户上下文
            UserContext userContext = userContextService.getUserContext(token);
            if (userContext == null) {
                throw new AuthException("访问令牌无效或已过期，请重新登录");
            }
            
            // 设置用户上下文到ThreadLocal
            UserContextHolder.setContext(userContext);
            
            return true;
            
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            log.error("登录拦截器处理异常", e);
            throw new AuthException("系统繁忙，请稍后再试");
        }
    }
    
    /**
     * 请求处理完成后执行
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                              Object handler, Exception ex) {
        // 清除用户上下文，避免内存泄漏
        UserContextHolder.clearContext();
    }
    
    /**
     * 从请求中提取令牌
     * 
     * @param request HTTP请求
     * @return 令牌字符串，如果未找到返回null
     */
    private String extractToken(HttpServletRequest request) {
        // 优先从请求头获取
        String authorization = request.getHeader(TOKEN_HEADER);
        if (StrUtil.isNotBlank(authorization) && authorization.startsWith(TOKEN_PREFIX)) {
            return authorization.substring(TOKEN_PREFIX.length());
        }
        
        return null;
    }
    
}
