package com.zzy.admin.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 平台属性值表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "attribute_values")
public class AttributeValues implements Serializable {
    /**
     * 属性值ID，主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 所属属性名ID，关联attribute_keys表的id
     */
    @TableField(value = "key_id")
    private Long keyId;

    /**
     * 属性值，例如：骁龙8 Gen 2、红色
     */
    @TableField(value = "`value`")
    private String value;

    /**
     * 0启用 1禁用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}