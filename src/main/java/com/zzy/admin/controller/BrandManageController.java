package com.zzy.admin.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzy.admin.common.Result;
import com.zzy.admin.domain.po.BrandManagement;
import com.zzy.admin.service.BrandManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName com.zzy.admin.controller
 *
 * @author zzy
 * @className ProductManageController
 * @date 2025/7/6
 * @description TODO
 */
@RestController
@RequestMapping("/product")
//@RequiredArgsConstructor
public class BrandManageController {

    @Autowired
    private BrandManagementService brandManagementService;

    @GetMapping("/getProductList/{pageNum}/{pageSize}")
    public Result<?> getProductList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BrandManagement> brandManagementList = brandManagementService.lambdaQuery()
                .eq(BrandManagement::getIsShow, 0)
                .orderByDesc(BrandManagement::getCreateTime)
                .orderByDesc(BrandManagement::getId)
                .list();
        PageInfo<BrandManagement> pageInfo = new PageInfo<>(brandManagementList);
        return Result.success(pageInfo);
    }
}
