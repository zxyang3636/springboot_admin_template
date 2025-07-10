package com.zzy.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    Enable(0, "启用"),
    Disable(1, "禁用");

    private final int code;
    private final String description;

    /**
     * 根据code获取对应的枚举值
     */
    public static StatusEnum fromCode(int code) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态码: " + code);
    }
}
