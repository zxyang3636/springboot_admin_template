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
    * 平台属性名表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "attribute_keys")
public class AttributeKeys implements Serializable {
    /**
     * 属性名ID，主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 所属分类ID，关联categories表的id
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 属性名，例如：CPU型号、颜色
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 排序值，数字越小越靠前
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;

    /**
     * 0 启用 1禁用
     */
    @TableField(value = "`status`")
    private Byte status;

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