package com.zzy.admin.utils;

import javax.annotation.PostConstruct;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;   

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import cn.hutool.core.util.StrUtil;
import com.zzy.admin.exception.AuthException;
import com.zzy.admin.config.JwtProperties;


@Slf4j
@Component
public class JwtUtil {
    @Autowired
    private JwtProperties jwtProperties; // 注入JKS配置

    private PrivateKey privateKey; // 用于签名的私钥
    private PublicKey publicKey;   // 用于验签的公钥


    /**
     * JWT 签名算法
     */
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.RS256;  // 使用RS256算法
    
    /**
     * JWT 中用户ID的键名
     */
    private static final String USER_ID_KEY = "userId";
    
    /**
     * JWT 中用户名的键名
     */
    private static final String USERNAME_KEY = "username";
    
    /**
     * JWT 中用户名的键名
     */
    private static final String NICKNAME_KEY = "nickname";

    /**
     * JWT 中令牌类型的键名
     */
    private static final String TOKEN_TYPE_KEY = "type";
    /**
     * JWT 中访问令牌类型的值
     */
    private static final String ACCESS_TOKEN_TYPE = "access";
    /**
     * JWT 中刷新令牌类型的值
     */
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    

    
    /**
     * 初始化方法，在Bean创建后执行
     */
    @PostConstruct
    public void init() {
        try {
            Resource resource = jwtProperties.getLocation();
            String password = jwtProperties.getPassword();
            String alias = jwtProperties.getAlias();

            if (resource == null || !resource.exists()) {
                throw new IllegalStateException("JWT密钥库文件未找到，请检查配置: " + jwtProperties.getLocation());
            }

            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (InputStream is = resource.getInputStream()) {
                keyStore.load(is, password.toCharArray());
            }

            // 从密钥库中获取私钥和公钥
            this.privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
            this.publicKey = keyStore.getCertificate(alias).getPublicKey();

            if (this.privateKey == null || this.publicKey == null) {
                throw new IllegalStateException("在JKS文件中找不到别名为 '" + alias + "' 的密钥对");
            }

            // log.info("JWT工具类初始化成功，使用RS256非对称加密。");

        } catch (Exception e) {
            log.error("初始化JWT工具类失败，无法加载JKS密钥库", e);
            // 抛出运行时异常，使服务启动失败，以便及时发现配置问题
            throw new RuntimeException("初始化JWT工具类失败，请检查JKS配置", e);
        }
    }
    
      /**
     * 生成 JWT 访问令牌
     */
    public String generateAccessToken(Long userId, String username, String nickname) {
        return generateToken(userId, username, nickname, jwtProperties.getExpiration(), ACCESS_TOKEN_TYPE);
    }

    /**
     * 生成 JWT 刷新令牌
     */
    public String generateRefreshToken(Long userId, String username, String nickname) {
        return generateToken(userId, username, nickname, jwtProperties.getRefresh(), REFRESH_TOKEN_TYPE);
    }

    /**
     * 通用令牌生成方法
     */
    private String generateToken(Long userId, String username, String nickname, Integer ttl, String tokenType) {
        if (userId == null || username == null || username.trim().isEmpty()) {
            throw new AuthException("用户ID和用户名不能为空");
        }

        try {
            Date now = new Date();
            Date expiration = new Date(now.getTime() + ttl * 60 * 60 * 1000);

            Map<String, Object> claims = new HashMap<>();
            claims.put(USER_ID_KEY, userId);
            claims.put(USERNAME_KEY, username);
            claims.put(NICKNAME_KEY, nickname);
            claims.put(TOKEN_TYPE_KEY, tokenType);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(this.privateKey, SIGNATURE_ALGORITHM) //  使用私钥和RS256签名
                    .compact();

        } catch (Exception e) {
            log.error("生成JWT失败，用户ID: {}, 用户名: {}", userId, username, e);
            throw new AuthException("生成令牌失败");
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
                    .setSigningKey(this.publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
        } catch (ExpiredJwtException e) {
            // log.warn("JWT令牌已过期: {}", e.getMessage());
            throw new AuthException("访问令牌已过期，请重新登录");
        } catch (UnsupportedJwtException e) {
            // log.warn("不支持的JWT令牌: {}", e.getMessage());
            throw new AuthException("不支持的令牌格式");
        } catch (MalformedJwtException e) {
            // log.warn("JWT令牌格式错误: {}", e.getMessage());
            throw new AuthException("令牌格式错误");
        } catch (SecurityException e) {
            // log.warn("JWT令牌签名验证失败: {}", e.getMessage());
            throw new AuthException("令牌签名验证失败");
        } catch (IllegalArgumentException e) {
            // log.warn("JWT令牌参数无效: {}", e.getMessage());
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
            // log.debug("JWT令牌验证失败: {}", e.getMessage());
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
        
        try {
            if (userId instanceof Number) {
                return ((Number) userId).longValue();
            } else if (userId instanceof String) {
                return Long.valueOf((String) userId);
            } else {
                throw new AuthException("令牌中用户ID类型无效: " + userId.getClass().getName());
            }
        } catch (NumberFormatException e) {
            throw new AuthException("令牌中用户ID格式错误: " + userId);
        }
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
     * 从令牌中获取用户昵称
     * 
     * @param token JWT令牌
     * @return 用户昵称
     */
    public String getNicknameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get(NICKNAME_KEY, String.class);
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
            String nickname = getNicknameFromToken(token);
            String newToken = generateAccessToken(userId, username, nickname);
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
