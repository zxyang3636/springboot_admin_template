package com.zzy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzy.admin.domain.po.AttributeKeys;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttributeKeysMapper extends BaseMapper<AttributeKeys> {
    int batchInsert(@Param("list") List<AttributeKeys> list);
}