package com.zzy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzy.admin.domain.po.BrandManagement;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BrandManagementMapper extends BaseMapper<BrandManagement> {
    int batchInsert(@Param("list") List<BrandManagement> list);
}