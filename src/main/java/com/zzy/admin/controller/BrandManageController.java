package com.zzy.admin.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzy.admin.common.Result;
import com.zzy.admin.domain.po.BrandManagement;
import com.zzy.admin.exception.ParamException;
import com.zzy.admin.service.BrandManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName com.zzy.admin.controller
 *
 * @author zzy
 * @className ProductManageController
 * @date 2025/7/6
 * @description 品牌管理
 */
@RestController
@RequestMapping("/brand")
// @RequiredArgsConstructor
public class BrandManageController {

    @Autowired
    private BrandManagementService brandManagementService;

    private Snowflake id = IdUtil.getSnowflake(23, 12);

    /**
     * 获取品牌列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getProductList/{pageNum}/{pageSize}")
    public Result<?> getProductList(@PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BrandManagement> brandManagementList = brandManagementService.lambdaQuery()
                .eq(BrandManagement::getIsShow, 0)
                .orderByDesc(BrandManagement::getCreateTime)
                .orderByDesc(BrandManagement::getId)
                .list();
        PageInfo<BrandManagement> pageInfo = new PageInfo<>(brandManagementList);
        return Result.success(pageInfo);
    }

    /**
     * 添加品牌
     *
     * @param brandManagement
     * @return
     */
    @PostMapping("/insertTrademark")
    public Result<?> insertTrademark(@RequestBody BrandManagement brandManagement) {
        if (StrUtil.isBlank(brandManagement.getBrandName()) || StrUtil.isBlank(brandManagement.getLogoUrl())) {
            throw new ParamException();
        }
        brandManagement.setId(id.nextId());
        return brandManagementService.save(brandManagement) ? Result.successMsg("添加成功") : Result.fail("添加失败");
    }

    /**
     * 删除品牌
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteTrademark/{id}")
    public Result<?> deleteTrademark(@PathVariable("id") Long id) {
        return brandManagementService.lambdaUpdate().eq(BrandManagement::getId, id).set(BrandManagement::getIsShow, 1)
                .update() ? Result.successMsg("删除成功") : Result.fail("删除失败");
    }

    /**
     * 修改品牌
     *
     * @param brandManagement
     * @return
     */
    @PutMapping("/updateTrademark")
    public Result<?> updateTrademark(@RequestBody BrandManagement brandManagement) {
        if (StrUtil.isBlank(brandManagement.getBrandName()) || StrUtil.isBlank(brandManagement.getLogoUrl())) {
            throw new ParamException();
        }
        return brandManagementService.updateById(brandManagement) ? Result.successMsg("修改成功") : Result.fail("修改失败");
    }
}
