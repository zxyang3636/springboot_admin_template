package com.zzy.admin.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户上下文信息
 * 
 * @author zzy
 * @date 2024/01/01
 * @description 保存当前登录用户的上下文信息，包括用户基本信息和权限信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户角色
     */
    private String role;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 会话令牌
     */
    private String token;

    /**
     * 权限列表（可选，根据需要添加）
     */
    private java.util.Set<String> permissions;
}
