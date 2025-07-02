package com.zzy.admin.utils;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zzy.admin.exception.AuthException;

@Slf4j
@Component
public class JwtUtil {

    /**
     * JWT 密钥（从配置文件读取，如果未配置则使用默认值）
     */
    @Value("${jwt.secret:gP#73YvUq!4rEwzX1@cN9kDf}")
    private String secretKey;
    
    /**
     * JWT 过期时间（小时）
     */
    @Value("${jwt.expiration:24}")
    private Integer expirationHours;
    
    /**
     * JWT 刷新时间（小时）
     */
    @Value("${jwt.refresh:168}")
    private Integer refreshHours;
    
    /**
     * JWT 签名算法使用的密钥
     */
    private SecretKey key;
    
    /**
     * JWT 签名算法
     */
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    
    /**
     * JWT 中用户ID的键名
     */
    private static final String USER_ID_KEY = "userId";
    
    /**
     * JWT 中用户名的键名
     */
    private static final String USERNAME_KEY = "username";
    

    

    
    /**
     * 初始化方法，在Bean创建后执行
     */
    @PostConstruct
    public void init() {
        // 确保密钥长度足够安全（至少32字符）
        if (secretKey.length() < 32) {
            secretKey = secretKey + "UoL@f8YP#rKWZ9XtE3bGjMCd76qvN1AsXz!Np2GcV@87jYKqRUM#wfLBtd9A3Er5";
        }
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
//        log.info("JWT工具类初始化完成，过期时间: {}小时, 刷新时间: {}小时", expirationHours, refreshHours);
    }
    
    /**
     * 生成 JWT 令牌
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT令牌字符串
     */
    public String generateAccessToken(Long userId, String username) {
        if (userId == null || StrUtil.isBlank(username)) {
            throw new AuthException("用户ID和用户名不能为空");
        }
        
        try {
            Date now = new Date();
            Date expiration = DateUtil.offsetHour(now, expirationHours);
            
            // 构建JWT载荷
            Map<String, Object> claims = new HashMap<>();
            claims.put(USER_ID_KEY, userId);
            claims.put(USERNAME_KEY, username);
            
            // 生成JWT
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(key, SIGNATURE_ALGORITHM)
                    .claim("type", "access")
                    .compact();
            
            log.debug("生成JWT成功，用户ID: {}, 用户名: {}, 过期时间: {}", userId, username, expiration);
            return token;
            
        } catch (Exception e) {
            log.error("生成JWT失败，用户ID: {}, 用户名: {}", userId, username, e);
            throw new AuthException("生成访问令牌失败");
        }
    }
    
    /**
     * 生成刷新令牌
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @return 刷新令牌字符串
     */
    public String generateRefreshToken(Long userId, String username) {
        if (userId == null || StrUtil.isBlank(username)) {
            throw new AuthException("用户ID和用户名不能为空");
        }
        
        try {
            Date now = new Date();
            Date expiration = DateUtil.offsetHour(now, refreshHours);
            
            // 刷新令牌只包含基本信息
            Map<String, Object> claims = new HashMap<>();
            claims.put(USER_ID_KEY, userId);
            claims.put(USERNAME_KEY, username);
            claims.put("type", "refresh");
            
            String refreshToken = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(key, SIGNATURE_ALGORITHM)
                    .claim("type", "refresh")
                    .compact();
            
            log.debug("生成刷新令牌成功，用户ID: {}, 用户名: {}, 过期时间: {}", userId, username, expiration);
            return refreshToken;
            
        } catch (Exception e) {
            log.error("生成刷新令牌失败，用户ID: {}, 用户名: {}", userId, username, e);
            throw new AuthException("生成刷新令牌失败");
        }
    }
    
    
    /**
     * 解析 JWT 令牌
     * 
     * @param token JWT令牌
     * @return Claims对象，包含令牌中的所有信息
     * @throws AuthException 当令牌无效、过期或解析失败时抛出
     */
    public Claims parseToken(String token) {
        if (StrUtil.isBlank(token)) {
            throw new AuthException("令牌不能为空");
        }
        
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
        } catch (ExpiredJwtException e) {
            log.warn("JWT令牌已过期: {}", e.getMessage());
            throw new AuthException("访问令牌已过期，请重新登录");
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的JWT令牌: {}", e.getMessage());
            throw new AuthException("不支持的令牌格式");
        } catch (MalformedJwtException e) {
            log.warn("JWT令牌格式错误: {}", e.getMessage());
            throw new AuthException("令牌格式错误");
        } catch (SecurityException e) {
            log.warn("JWT令牌签名验证失败: {}", e.getMessage());
            throw new AuthException("令牌签名验证失败");
        } catch (IllegalArgumentException e) {
            log.warn("JWT令牌参数无效: {}", e.getMessage());
            throw new AuthException("令牌参数无效");
        } catch (Exception e) {
            log.error("解析JWT令牌异常", e);
            throw new AuthException("令牌解析失败");
        }
    }
    
    /**
     * 验证 JWT 令牌是否有效
     * 
     * @param token JWT令牌
     * @return true-有效，false-无效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (AuthException e) {
            log.debug("JWT令牌验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 从令牌中获取用户ID
     * 
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        Object userId = claims.get(USER_ID_KEY);
        
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        } else if (userId instanceof Long) {
            return (Long) userId;
        } else if (userId instanceof String) {
            return Long.valueOf((String) userId);
        }
        
        throw new AuthException("令牌中用户ID格式错误");
    }
    
    /**
     * 从令牌中获取用户名
     * 
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get(USERNAME_KEY, String.class);
    }
    

    
    /**
     * 获取令牌的过期时间
     * 
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }
    
    /**
     * 获取令牌的签发时间
     * 
     * @param token JWT令牌
     * @return 签发时间
     */
    public Date getIssuedAtFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getIssuedAt();
    }
    
    /**
     * 检查令牌是否即将过期（距离过期时间小于1小时）
     * 
     * @param token JWT令牌
     * @return true-即将过期，false-还有较长时间
     */
    public boolean isTokenExpiringSoon(String token) {
        try {
            Date expiration = getExpirationFromToken(token);
            Date now = new Date();
            long timeDiff = expiration.getTime() - now.getTime();
            // 距离过期时间小于1小时（3600000毫秒）
            return timeDiff < 3600000;
        } catch (Exception e) {
            log.warn("检查令牌过期时间失败", e);
            return true;
        }
    }
    
    /**
     * 刷新令牌（基于现有令牌生成新令牌）
     * 
     * @param token 现有的JWT令牌
     * @return 新的JWT令牌
     */
    public String refreshToken(String token) {
        try {
            parseToken(token);
            
            Long userId = getUserIdFromToken(token);
            String username = getUsernameFromToken(token);
            
            String newToken = generateAccessToken(userId, username);
            log.info("刷新JWT令牌成功，用户ID: {}, 用户名: {}", userId, username);
            
            return newToken;
            
        } catch (Exception e) {
            log.error("刷新JWT令牌失败", e);
            throw new AuthException("刷新令牌失败");
        }
    }
    
    /**
     * 获取令牌中的所有声明信息
     * 
     * @param token JWT令牌
     * @return 包含所有声明的Map
     */
    public Map<String, Object> getAllClaimsFromToken(String token) {
        Claims claims = parseToken(token);
        return new HashMap<>(claims);
    }
    
    /**
     * 检查令牌类型是否为刷新令牌
     * 
     * @param token JWT令牌
     * @return true-是刷新令牌，false-是访问令牌
     */
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = parseToken(token);
            return "refresh".equals(claims.get("type"));
        } catch (Exception e) {
            return false;
        }
    }

}
