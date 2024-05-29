package cn.yrm.tools.alioss.client;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.yrm.tools.common.service.IFileUploader;
import com.aliyun.oss.OSSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

@Slf4j
public class AliOssClient implements IFileUploader {

    private OSSClient client;
    private String bucket;
    private String endpoint;

    public AliOssClient(String endpoint, String accessKeyId, String accessKeySecret, String bucket) {
        this.bucket = bucket;
        this.endpoint = endpoint;
        this.client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public String upload(String filePath, InputStream inputStream) {
        this.client.putObject(bucket, filePath, inputStream);
        return "https://" + bucket + "." + endpoint + "/" + filePath;
    }

    @Override
    public String uploadFile(String biz, MultipartFile file) throws IOException {
        return saveFile(biz, file.getBytes(), file.getOriginalFilename());
    }

    @Override
    public String uploadImage(String biz, String base64Image) {
        byte[] fileBytes = Base64.decode(base64Image);
        return saveFile(biz, fileBytes, null);
    }

    @Override
    public byte[] readFile(String url) {
        // 读文件交给OSS
        return null;
    }

    @Override
    public String saveFile(String biz, byte[] fileBytes, String fileName) {
        String today = DateUtil.format(new Date(), "yyyyMMdd");
        String savePath = biz + "/" + today;
        String suffix = "jpg";
        if (StrUtil.isEmpty(fileName)) {
            suffix = FileTypeUtil.getType(new ByteArrayInputStream(fileBytes));
        } else if (fileName.indexOf(".") != -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String saveKey = savePath + StrUtil.format("/{}.{}", RandomUtil.randomString(32), suffix);
        return upload(saveKey, new ByteArrayInputStream(fileBytes));
    }


    /**
     * 获取签名
     *
     * @param filePath
     * @return
     */
    @Override
    public HashMap<String, String> getSign(String filePath) {
        return null;
    }
}
