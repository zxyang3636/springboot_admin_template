package com.zzy.admin.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 商品分类表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "categories")
public class Categories implements Serializable {
    /**
     * 分类ID，主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 父分类ID，一级分类此字段为NULL
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 分类名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 分类层级，1-一级分类，2-二级分类，3-三级分类
     */
    @TableField(value = "`level`")
    private Byte level;

    /**
     * 排序值，数字越小越靠前
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;

    /**
     * 状态：0-启用，1-禁用
     */
    @TableField(value = "`status`")
    private Boolean status;

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