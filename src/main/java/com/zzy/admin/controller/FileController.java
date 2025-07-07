package com.zzy.admin.controller;

import com.zzy.admin.common.Result;
import com.zzy.admin.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName com.zzy.admin.controller
 *
 * @author zzy
 * @className FileController
 * @date 2025/7/7
 * @description TODO
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/uploadImg")
    public Result<?> uploadImg(@RequestParam("file") MultipartFile file) {
        return fileService.uploadImg(file);
    }
}
