package com.zzy.admin.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zzy.admin.common.Result;
import com.zzy.admin.domain.dto.AttributeDTO;
import com.zzy.admin.domain.po.AttributeKeys;
import com.zzy.admin.domain.po.AttributeValues;
import com.zzy.admin.domain.vo.AttributeVO;
import com.zzy.admin.enums.StatusEnum;
import com.zzy.admin.exception.BusinessException;
import com.zzy.admin.mapper.AttributeKeysMapper;
import com.zzy.admin.mapper.AttributeValuesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import com.zzy.admin.mapper.CategoriesMapper;
import com.zzy.admin.domain.po.Categories;
import com.zzy.admin.service.CategoriesService;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories> implements CategoriesService {
    private final CategoriesMapper categoriesMapper;
    private final AttributeValuesMapper attributeValuesMapper;
    private final AttributeKeysMapper attributeKeysMapper;
    private final Snowflake ID = IdUtil.createSnowflake(3, 6);

    @Override
    public int batchInsert(List<Categories> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public Result<?> getFirstLevelCategory() {
        List<Categories> list = lambdaQuery().eq(Categories::getLevel, 1)
                .eq(Categories::getStatus, StatusEnum.Enable)
                .orderByDesc(Categories::getSortOrder)
                .list();
        return Result.success(list);
    }

    @Override
    public Result<?> getSecondLevelCategory(String id) {
        List<Categories> list = lambdaQuery().eq(Categories::getLevel, 2)
                .eq(Categories::getStatus, StatusEnum.Enable)
                .eq(Categories::getParentId, id)
                .orderByDesc(Categories::getSortOrder)
                .list();
        return Result.success(list);
    }

    @Override
    public Result<?> getThirdLevelCategory(String id) {
        List<Categories> list = lambdaQuery().eq(Categories::getLevel, 3)
                .eq(Categories::getStatus, StatusEnum.Enable)
                .eq(Categories::getParentId, id)
                .orderByDesc(Categories::getSortOrder)
                .list();
        return Result.success(list);
    }

    @Override
    public Result<?> getAttributeInfo(String id) {
        List<AttributeVO> attributeVOList = categoriesMapper.getAttributeInfo(id); // 拿到重复项
        List<Long> attributeKeysId = attributeVOList.stream()
                .map(AttributeVO::getAttributeKeysId)
                .distinct()
                .collect(Collectors.toList());
        if (attributeKeysId.size() == 0) {
            throw new BusinessException("暂无数据");
        }
        List<AttributeValues> attributeValues = attributeValuesMapper.selectList(
                Wrappers.<AttributeValues>lambdaQuery().in(AttributeValues::getKeyId, attributeKeysId));

        // 构建 keyId -> List<value>
        Map<Long, List<String>> valueMap = attributeValues.stream()
                .collect(Collectors.groupingBy(
                        AttributeValues::getKeyId,
                        Collectors.mapping(AttributeValues::getValue, Collectors.toList())
                ));

        // 去重并设置 value
        Map<Long, AttributeVO> grouped = new LinkedHashMap<>();
        for (AttributeVO vo : attributeVOList) {
            Long keyId = vo.getAttributeKeysId();
            if (!grouped.containsKey(keyId)) {
                vo.setValue(new ArrayList<>());
                grouped.put(keyId, vo);
            }
            List<String> values = valueMap.getOrDefault(keyId, Collections.emptyList());
            vo.setValue(values);
        }

        return Result.success(grouped.values());
    }

    @Override
    public Result<?> updateOrSaveAttribute(AttributeDTO attributeDTO) {
        String thirdLevelId = attributeDTO.getThirdLevelId();
        if (StrUtil.isNotBlank(thirdLevelId)) { // 添加
            List<String> valueList = attributeDTO.getValue();
            String name = attributeDTO.getName();
            // 保存keys
            long keysId = ID.nextId();
            AttributeKeys attributeKeys = AttributeKeys.builder().id(keysId).categoryId(Long.parseLong(thirdLevelId)).name(name).build();
            attributeKeysMapper.insert(attributeKeys);
            // 保存values
            ArrayList<AttributeValues> collectAttributeValues = new ArrayList<>();
            for (String value : valueList) {
                AttributeValues attributeValues = AttributeValues.builder().id(ID.nextId()).keyId(keysId).value(value).createTime(new Date()).updateTime(new Date()).build();
                collectAttributeValues.add(attributeValues);
            }
            attributeValuesMapper.batchInsert(collectAttributeValues);
        }
        return Result.successMsg("添加成功");
    }
}
