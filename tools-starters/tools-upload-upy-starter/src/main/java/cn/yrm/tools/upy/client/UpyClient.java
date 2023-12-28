package cn.yrm.tools.upy.client;


import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yrm.tools.common.exception.BusinessException;
import cn.yrm.tools.common.service.IFileUploader;
import cn.yrm.tools.common.util.DateUtil;
import cn.yrm.tools.upy.autoconfigure.UpyProperties;
import com.upyun.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 又拍云工具类
 * </p>
 *
 * @author yiyun
 * @since 2019-03-15
 */
@Slf4j
public class UpyClient implements IFileUploader {

    @Autowired
    UpyProperties upyProperties;

    /**
     * 获取又拍云签名
     *
     * @param path 文件存储父路径
     * @return {ResultDTO}
     */
    @Override
    public HashMap<String, String> getSign(String path) {

        String date = DateUtil.getGmtString(new Date());
        String saveKey = path + "/{random32}.{suffix}";

        HashMap<String, Object> map = new HashMap<>(5);
        map.put(Params.BUCKET, upyProperties.getBucket());
        map.put(Params.SAVE_KEY, saveKey);
        map.put(Params.EXPIRATION, String.valueOf(System.currentTimeMillis() / 1000L + upyProperties.getExpiration()));
        map.put(Params.DATE, date);
        map.put(Params.CONTENT_MD5, "");

        String policy = UpYunUtils.getPolicy(map);
        String authorization = null;
        try {
            authorization = UpYunUtils.sign("POST", date, "/" + upyProperties.getBucket(),
                    upyProperties.getOperator(), UpYunUtils.md5(upyProperties.getPassword()), policy);
            HashMap<String, String> retMap = new HashMap<>(4);
            retMap.put("authorization", authorization);
            retMap.put("policy", policy);
            retMap.put("date", date);
            retMap.put("serviceUrl", upyProperties.getServiceUrl() + upyProperties.getBucket());
            retMap.put("imageHost", upyProperties.getImageHost());
            return retMap;
        } catch (UpException e) {
            log.error("获取又拍云签名失败", e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public String uploadFile(String biz, MultipartFile file) throws IOException {
        return saveFile(biz, file.getBytes(), file.getOriginalFilename());
    }

    @Override
    public String uploadImage(String biz, String base64Image) {
        byte[] fileBytes = Base64Coder.decode(base64Image);
        return saveFile(biz, fileBytes, null);
    }

    @Override
    public byte[] readFile(String url) {
        // 读文件交给又拍云Http
        return null;
    }

    @Override
    public String saveFile(String biz, byte[] fileBytes, String fileName) {
        String today = cn.hutool.core.date.DateUtil.format(new Date(), "yyyyMMdd");
        String savePath = "/" + biz + "/" + today;
        String suffix = "jpg";
        if (StrUtil.isEmpty(fileName)) {
            suffix = FileTypeUtil.getType(new ByteArrayInputStream(fileBytes));
        } else if (fileName.indexOf(".") != -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String saveKey = savePath + "/{random32}." + suffix;
        HashMap<String, Object> paramsMap = new HashMap<>(1);
        paramsMap.put(Params.SAVE_KEY, saveKey);
        try {
            FormUploader uploader = new FormUploader(upyProperties.getBucket(), upyProperties.getOperator()
                    , upyProperties.getPassword());
            Result result = uploader.upload(paramsMap, fileBytes);
            if (result.isSucceed()) {
                JSONObject jsonObject = JSONUtil.parseObj(result.getMsg());
                String url = jsonObject.get("url", String.class);
                return upyProperties.getImageHost() + url;
            } else {
                log.error("又拍云上传文件失败", result.getMsg());
                throw new BusinessException(result.getMsg());
            }
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            log.error("又拍云上传文件失败", e);
            throw new BusinessException(e.getMessage());
        }
    }
}
