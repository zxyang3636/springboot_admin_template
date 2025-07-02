package com.zzy.admin.common;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户上下文持有者
 *
 * @author zzy
 * @date 2024/01/01
 * @description 基于ThreadLocal管理当前线程的用户上下文信息
 */
@Slf4j
public class UserContextHolder {

    /**
     * 用户上下文线程本地变量
     */
    private static final ThreadLocal<UserContext> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置用户上下文
     *
     * @param userContext 用户上下文信息
     */
    public static void setContext(UserContext userContext) {
        if (userContext == null) {
            log.warn("设置用户上下文时传入了null值");
            return;
        }

        CONTEXT_HOLDER.set(userContext);}

    /**
     * 获取当前用户上下文
     *
     * @return 用户上下文信息，如果未登录返回null
     */
    public static UserContext getContext() {
        return (UserContext) CONTEXT_HOLDER.get();
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID，如果未登录返回null
     */
    public static Long getCurrentUserId() {
        UserContext context = getContext();
        return context != null ? context.getUserId() : null;
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名，如果未登录返回null
     */
    public static String getCurrentUsername() {
        UserContext context = getContext();
        return context != null ? context.getUsername() : null;
    }



    /**
     * 检查是否已登录
     *
     * @return true-已登录，false-未登录
     */
    public static boolean isLogin() {
        return getContext() != null;
    }

    /**
     * 清除当前用户上下文
     */
    public static void clearContext() {
        UserContext context = (UserContext) CONTEXT_HOLDER.get();
        if (context != null) {
            log.debug("清除用户上下文，用户ID: {}, 用户名: {}",
                    context.getUserId(), context.getUsername());
        }
        CONTEXT_HOLDER.remove();
    }
}
