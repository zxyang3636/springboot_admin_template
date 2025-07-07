package com.zzy.admin.service;

import com.zzy.admin.common.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName com.zzy.admin.service
 *
 * @author zzy
 * @className FileService
 * @date 2025/7/7
 * @description TODO
 */
public interface FileService {
    Result<?> uploadImg(MultipartFile file);
}
