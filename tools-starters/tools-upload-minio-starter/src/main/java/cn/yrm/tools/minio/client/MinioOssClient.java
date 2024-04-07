package cn.yrm.tools.minio.client;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.yrm.tools.common.exception.BusinessException;
import cn.yrm.tools.common.service.IFileUploader;
import cn.yrm.tools.minio.autoconfigure.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 * 又拍云工具类
 * </p>
 *
 * @author yiyun
 * @since 2019-03-15
 */
@Slf4j
public class MinioOssClient implements IFileUploader {

    @Autowired
    MinioProperties minioProperties;

    private MinioClient minioClient;

    public MinioOssClient(String endpoint, String accessKey, String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }


    /**
     * 获取又拍云签名
     *
     * @param path 文件存储父路径
     * @return {ResultDTO}
     */
    @Override
    public HashMap<String, String> getSign(String path) {
        return null;
    }

    @Override
    public String uploadFile(String biz, MultipartFile file) throws IOException {
        String filePath = getSavePath(biz, file.getBytes(), file.getOriginalFilename());
        return saveFile(filePath, file.getInputStream(), file.getSize(), file.getContentType());
    }

    @Override
    public String uploadImage(String biz, String base64Image) {
        byte[] fileBytes = Base64.decode(base64Image);
        return saveFile(biz, fileBytes, null);
    }

    @Override
    public byte[] readFile(String url) {
        // 读文件交给nginx
        return null;
    }

    @Override
    public String saveFile(String biz, byte[] fileBytes, String fileName) {
        String filePath = getSavePath(biz, fileBytes, null);
        return saveFile(filePath, new ByteArrayInputStream(fileBytes), fileBytes.length, getContentType(filePath));
    }

    private String getSavePath(String biz, byte[] fileBytes, String fileName) {
        String today = DateUtil.format(new Date(), "yyyyMMdd");
        String savePath = biz + "/" + today;
        String suffix = "jpg";
        if (StrUtil.isEmpty(fileName)) {
            suffix = FileTypeUtil.getType(new ByteArrayInputStream(fileBytes));
        } else if (fileName.indexOf(".") != -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String saveKey = savePath + StrUtil.format("/{}.{}", RandomUtil.randomString(32), suffix);
        return saveKey;
    }

    private String getContentType(String filePath) {
        if (filePath.endsWith("jpg") || filePath.endsWith("jpeg")) {
            return "image/jpeg";
        }
        if (filePath.endsWith("png")) {
            return "image/png";
        }
        return "";
    }

    private String saveFile(String filePath, InputStream inputStream, long fileSize, String contentType) {
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(filePath)
                .stream(inputStream, fileSize, -1)
                .contentType(contentType).build();
        try {
            minioClient.putObject(objectArgs);
            return StrUtil.format("{}/{}/{}", minioProperties.getImageHost(),
                    minioProperties.getBucket(), filePath);
        } catch (Exception e) {
            log.error("Minio上传文件失败", e);
            throw new BusinessException(e.getMessage());
        }
    }
}
