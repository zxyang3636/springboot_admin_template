package com.zzy.admin.service;

import java.util.List;

import com.zzy.admin.common.Result;
import com.zzy.admin.domain.dto.AttributeDTO;
import com.zzy.admin.domain.po.Categories;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.admin.domain.vo.AttributeVO;

public interface CategoriesService extends IService<Categories> {


    int batchInsert(List<Categories> list);

    Result<?> getFirstLevelCategory();

    Result<?> getSecondLevelCategory(String id);

    Result<?> getThirdLevelCategory(String id);

    Result<?> getAttributeInfo(String id);

    Result<?> updateOrSaveAttribute(AttributeDTO attributeDTO);
}
