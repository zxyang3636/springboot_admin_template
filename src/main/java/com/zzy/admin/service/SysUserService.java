package com.zzy.admin.service;

import java.util.List;

import com.zzy.admin.common.Result;
import com.zzy.admin.domain.dto.RefreshRequest;
import com.zzy.admin.domain.po.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SysUserService extends IService<SysUser>{


    int batchInsert(List<SysUser> list);

    Result<?> login(SysUser sysUser);

    Result<?> refreshToken(RefreshRequest refreshRequest);
}
