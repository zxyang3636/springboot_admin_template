package com.zzy.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.admin.mapper.BrandManagementMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import com.zzy.admin.domain.po.BrandManagement;
import com.zzy.admin.service.BrandManagementService;

@Service
public class BrandManagementServiceImpl extends ServiceImpl<BrandManagementMapper, BrandManagement> implements BrandManagementService {

    @Override
    public int batchInsert(List<BrandManagement> list) {
        return baseMapper.batchInsert(list);
    }
}
