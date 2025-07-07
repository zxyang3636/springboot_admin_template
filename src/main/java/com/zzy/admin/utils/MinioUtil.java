package com.zzy.admin.utils;

import com.zzy.admin.common.Result;
import com.zzy.admin.config.MinioProp;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * packageName com.zzy.admin.utils
 *
 * @author zzy
 * @className MinioUtil
 * @date 2025/7/7
 * @description TODO
 */
@Slf4j
@Component
public class MinioUtil {
    @Autowired
    private MinioClient client;
    @Autowired
    private MinioProp minioProp;

    /**
     * 创建bucket
     */
    public void createBucket(String bucketName) {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
        try {
            if (client.bucketExists(bucketExistsArgs))
                return;
            client.makeBucket(makeBucketArgs);
        } catch (Exception e) {
            log.error("创建桶失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * @param file       文件
     * @param bucketName 存储桶
     * @return
     */
    public com.zzy.admin.common.Result<?> uploadFile(MultipartFile file, String bucketName) throws Exception {
        // 判断上传文件是否为空
        if (null == file || 0 == file.getSize()) {
            return com.zzy.admin.common.Result.fail();
        }
        // 判断存储桶是否存在
        createBucket(bucketName);
        // 文件名
        String originalFilename = file.getOriginalFilename();
        // 新的文件名 = 存储桶名称_时间戳.后缀名
        String fileName = bucketName + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
        // 开始上传
        InputStream inputStream = file.getInputStream();
        PutObjectArgs args = PutObjectArgs.builder().bucket(bucketName).object(fileName)
                .stream(inputStream, inputStream.available(), -1).build();
        client.putObject(args); //上传服务中(minio)
        String imgPath = minioProp.getEndpoint() + "/" + bucketName + "/" + fileName;
        return Result.success(imgPath);
    }
}
