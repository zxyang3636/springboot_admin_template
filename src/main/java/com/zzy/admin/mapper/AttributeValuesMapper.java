package com.zzy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzy.admin.domain.po.AttributeValues;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttributeValuesMapper extends BaseMapper<AttributeValues> {
    int batchInsert(@Param("list") List<AttributeValues> list);
}