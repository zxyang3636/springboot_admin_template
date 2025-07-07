package com.zzy.admin.service.impl;

import com.zzy.admin.common.Result;
import com.zzy.admin.service.FileService;
import com.zzy.admin.utils.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.SameLen;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName com.zzy.admin.service.impl
 *
 * @author zzy
 * @className FileServiceImpl
 * @date 2025/7/7
 * @description TODO
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    private final MinioUtil minioUtil;

    @Override
    public Result<?> uploadImg(MultipartFile file) {
        try {
            return minioUtil.uploadFile(file, "img");
        } catch (Exception e) {
            log.error("minio图片上传失败", e);
            throw new RuntimeException(e);
        }
    }
}
