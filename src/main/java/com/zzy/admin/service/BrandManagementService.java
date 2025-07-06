package com.zzy.admin.service;

import java.util.List;

import com.zzy.admin.domain.po.BrandManagement;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BrandManagementService extends IService<BrandManagement> {


    int batchInsert(List<BrandManagement> list);

}
