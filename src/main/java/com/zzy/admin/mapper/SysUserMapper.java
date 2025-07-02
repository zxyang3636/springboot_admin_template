package com.zzy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzy.admin.domain.po.SysUser;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    int batchInsert(@Param("list") List<SysUser> list);
}