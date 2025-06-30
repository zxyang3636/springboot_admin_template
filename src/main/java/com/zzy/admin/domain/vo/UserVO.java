package com.zzy.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName com.zzy.admin.domain.vo
 *
 * @author zzy
 * @className UserVO
 * @date 2025/6/30
 * @description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVO {
    private String token;
    private String avatar;
    private String username;
}
