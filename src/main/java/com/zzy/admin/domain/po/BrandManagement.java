package com.zzy.admin.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "brand_management")
public class BrandManagement implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField(value = "brand_name")
    private String brandName;

    @TableField(value = "logo_url")
    private String logoUrl;

    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 0展示 1删除
     */
    @TableField(value = "is_show")
    private Integer isShow;

    private static final long serialVersionUID = 1L;
}