package com.zzy.admin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * packageName com.zzy.admin.domain.vo
 *
 * @author zzy
 * @className AttributeVO
 * @date 2025/7/10
 * @description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeDTO {
    private Long attributeKeysId;
    private String name;
    private List<String> value;
    private String thirdLevelId;
}
