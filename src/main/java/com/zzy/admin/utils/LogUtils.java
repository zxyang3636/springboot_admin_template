package com.zzy.admin.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志工具类
 */
@Slf4j
public class LogUtils {

    /**
     * 敏感参数字段
     */
    private static final List<String> SENSITIVE_FIELDS = Arrays.asList(
            "password", "accessToken", "refreshToken", "key");

    /**
     * 获取客户端IP地址
     */
    public static String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes == null) {
                return "unknown";
            }
            HttpServletRequest request = attributes.getRequest();
            return getClientIp(request);
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * 获取客户端IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取请求URL
     */
    public static String getRequestUrl() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes == null) {
                return "";
            }
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURL().toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取请求方式
     */
    public static String getRequestMethod() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes == null) {
                return "";
            }
            HttpServletRequest request = attributes.getRequest();
            return request.getMethod();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化异常信息
     */
    public static String formatException(Throwable e) {
        if (e == null) {
            return "";
        }
        String message = e.getMessage();
        return message != null && message.length() > 500 ? message.substring(0, 500) + "..." : message;
    }

    /**
     * 过滤不可序列化的参数，保留有用信息
     */
    private static Object[] filterUnserializableObjects(Object[] args) {
        if (args == null || args.length == 0) {
            return args;
        }

        Object[] filteredArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                filteredArgs[i] = null;
                continue;
            }

            // 处理特殊对象
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) arg;
                // 提取请求中的有用信息
                Map<String, String> requestInfo = new HashMap<>();
                requestInfo.put("method", request.getMethod());
                requestInfo.put("requestURI", request.getRequestURI());
                requestInfo.put("queryString", request.getQueryString());
                requestInfo.put("contentType", request.getContentType());
                filteredArgs[i] = requestInfo;
            }
            // 处理其他不可序列化对象
            else if (!(arg instanceof java.io.Serializable)) {
                // 尝试提取对象的基本信息
                Map<String, String> info = new HashMap<>();
                info.put("className", arg.getClass().getSimpleName());
                info.put("toString", arg.toString());
                filteredArgs[i] = info;
            } else {
                filteredArgs[i] = arg;
            }
        }
        return filteredArgs;
    }

    /**
     * 参数转JSON字符串
     */
    public static String argsToString(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        try {
            // 先过滤不可序列化的对象，但保留有用信息
            Object[] filteredArgs = filterUnserializableObjects(args);
            // 过滤敏感数据
            filteredArgs = filterSensitiveData(filteredArgs);

            // 使用 FastJSON2 序列化，添加必要的特性
            String json = JSON.toJSONString(
                    filteredArgs,
                    JSONWriter.Feature.IgnoreErrorGetter,
                    JSONWriter.Feature.ReferenceDetection,
                    JSONWriter.Feature.NotWriteRootClassName,
                    JSONWriter.Feature.WriteMapNullValue // 保留空值
            );

            // 限制长度
            return json.length() > 2000 ? json.substring(0, 2000) + "..." : json;
        } catch (Exception e) {
            log.error("Failed to serialize params with FastJSON2", e);
            return "[Serialization Error]";
        }
    }

    /**
     * 结果转JSON字符串
     */
    public static String resultToString(Object result) {
        if (result == null) {
            return "";
        }
        try {
            Object processedResult;
            if (result instanceof java.io.Serializable) {
                processedResult = result;
            } else {
                // 提取不可序列化对象的有用信息
                Map<String, String> info = new HashMap<>();
                info.put("className", result.getClass().getSimpleName());
                info.put("toString", result.toString());
                processedResult = info;
            }

            String json = JSON.toJSONString(
                    processedResult,
                    JSONWriter.Feature.IgnoreErrorGetter,
                    JSONWriter.Feature.ReferenceDetection,
                    JSONWriter.Feature.NotWriteRootClassName,
                    JSONWriter.Feature.WriteMapNullValue // 保留空值
            );

            // 限制长度
            return json.length() > 2000 ? json.substring(0, 2000) + "..." : json;
        } catch (Exception e) {
            log.error("Failed to serialize result with FastJSON2", e);
            return "[Serialization Error]";
        }
    }

    /**
     * 过滤敏感数据
     */
    private static Object[] filterSensitiveData(Object[] args) {
        if (args == null || args.length == 0) {
            return args;
        }

        Object[] filteredArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                filteredArgs[i] = null;
                continue;
            }

            // 如果是Map类型，处理Map中的敏感字段
            if (arg instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = new HashMap<>((Map<String, Object>) arg);
                for (String sensitiveField : SENSITIVE_FIELDS) {
                    if (map.containsKey(sensitiveField)) {
                        map.put(sensitiveField, "******");
                    }
                }
                filteredArgs[i] = map;
            }
            // 如果是普通对象，尝试转换成Map处理
            else {
                try {
                    // 将对象转换为Map
                    String jsonString = JSON.toJSONString(arg);
                    Map<String, Object> map = JSON.parseObject(jsonString, Map.class);
                    if (map != null) {
                        // 处理敏感字段
                        for (String sensitiveField : SENSITIVE_FIELDS) {
                            if (map.containsKey(sensitiveField)) {
                                map.put(sensitiveField, "******");
                            }
                        }
                        filteredArgs[i] = map;
                    } else {
                        filteredArgs[i] = arg;
                    }
                } catch (Exception e) {
                    // 如果转换失败，保持原样
                    filteredArgs[i] = arg;
                }
            }
        }
        return filteredArgs;
    }

}
