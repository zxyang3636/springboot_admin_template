package com.zzy.admin.service;

import java.util.List;

import com.zzy.admin.common.Result;
import com.zzy.admin.domain.po.Categories;
import com.baomidou.mybatisplus.extension.service.IService;
public interface CategoriesService extends IService<Categories>{


    int batchInsert(List<Categories> list);

    Result<?> getFirstLevelCategory();

    Result<?> getSecondLevelCategory(String id);

    Result<?> getThirdLevelCategory(String id);

    Result<?> getAttributeInfo(String id);
}
