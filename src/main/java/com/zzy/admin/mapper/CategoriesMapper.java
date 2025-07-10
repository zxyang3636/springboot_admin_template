package com.zzy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzy.admin.domain.po.Categories;
import java.util.List;

import com.zzy.admin.domain.vo.AttributeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CategoriesMapper extends BaseMapper<Categories> {
    int batchInsert(@Param("list") List<Categories> list);

    List<AttributeVO> getAttributeInfo(String id);
}