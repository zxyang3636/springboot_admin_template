package com.zzy.admin.controller;

import com.zzy.admin.common.Result;
import com.zzy.admin.domain.dto.AttributeDTO;
import com.zzy.admin.domain.vo.AttributeVO;
import com.zzy.admin.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * packageName com.zzy.admin.controller
 *
 * @author zzy
 * @className AttributeController
 * @date 2025/7/10
 * @description TODO
 */
@RestController
@RequestMapping("/attr")
@RequiredArgsConstructor
public class AttributeController {
    private final CategoriesService categoriesService;

    /**
     * @return com.zzy.admin.common.Result<?>
     * @author zxyang3636
     * @date 2025/7/10
     * {@link Result<?>}
     * @description 获取一级分类
     */
    @GetMapping("/getFirstLevelCategory")
    public Result<?> getFirstLevelCategory() {
        return categoriesService.getFirstLevelCategory();
    }

    /**
     * @param id: [id]
     * @return com.zzy.admin.common.Result<?>
     * @author zxyang3636
     * @date 2025/7/10
     * {@link Result<?>}
     * @description 获取二级
     */
    @GetMapping("/getSecondLevelCategory/{id}")
    public Result<?> getSecondLevelCategory(@PathVariable("id") String id) {
        return categoriesService.getSecondLevelCategory(id);
    }

    /**
     * @param id: [id]
     * @return com.zzy.admin.common.Result<?>
     * @author zxyang3636
     * @date 2025/7/10
     * {@link Result<?>}
     * @description 获取三级分类
     */
    @GetMapping("/getThirdLevelCategory/{id}")
    public Result<?> getThirdLevelCategory(@PathVariable("id") String id) {
        return categoriesService.getThirdLevelCategory(id);
    }

    /**
     * @param id: [id]
     * @return com.zzy.admin.common.Result<?>
     * @author zxyang3636
     * @date 2025/7/10
     * {@link Result<?>}
     * @description 获取属性信息
     */
    @GetMapping("/getAttributeInfo/{id}")
    public Result<?> getAttributeInfo(@PathVariable("id") String id) {
        return categoriesService.getAttributeInfo(id);
    }

    /**
     * @param attributeDTO: [attributeDTO]
     * @return com.zzy.admin.common.Result<?>
     * @author zxyang3636
     * @date 2025/7/14
     * {@link Result<?>}
     * @description 添加或修改
     */
    @PostMapping("/updateOrSaveAttribute")
    public Result<?> updateOrSaveAttribute(@RequestBody AttributeDTO attributeDTO) {
        return categoriesService.updateOrSaveAttribute(attributeDTO);
    }

    @DeleteMapping("/del/{attributeKeysId}")
    public Result<?> delAttr(@PathVariable("attributeKeysId") Long attributeKeysId) {
        return categoriesService.delAttr(attributeKeysId);
    }
}
