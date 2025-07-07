package com.zzy.admin.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码加密工具类
 * 使用BCrypt算法进行密码哈希，自动生成随机盐值
 * 
 * @author zzy
 * @date 2024-12-25
 */
@Component
public class PasswordUtils {
    
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(12); // 强度12

    /**
     * 对密码进行BCrypt哈希加密
     * 
     * @param rawPassword 原始密码
     * @return BCrypt哈希后的密码（包含盐值）
     */
    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /**
     * 验证密码是否匹配
     * 
     * @param rawPassword 原始密码
     * @param encodedPassword BCrypt哈希后的密码
     * @return true表示密码匹配，false表示不匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }

    /**
     * 检查密码强度
     * 
     * @param password 密码
     * @return true表示密码强度足够，false表示密码过弱
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // 检查是否包含大小写字母、数字、特殊字符
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        
        // 至少包含三种类型的字符
        int typeCount = 0;
        if (hasLower) typeCount++;
        if (hasUpper) typeCount++;
        if (hasDigit) typeCount++;
        if (hasSpecial) typeCount++;
        
        return typeCount >= 3;
    }
} 