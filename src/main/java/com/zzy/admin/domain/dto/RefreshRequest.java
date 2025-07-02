package com.zzy.admin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName com.zzy.admin.domain.dto
 *
 * @author zzy
 * @className RefreshRequest
 * @date 2025/7/2
 * @description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshRequest {
    private String refreshToken;
}
