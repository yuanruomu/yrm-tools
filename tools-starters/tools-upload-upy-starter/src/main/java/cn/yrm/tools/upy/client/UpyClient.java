package cn.yrm.tools.upy.client;


import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yrm.tools.common.service.IFileUploader;
import cn.yrm.tools.common.util.DateUtil;
import cn.yrm.tools.upy.autoconfigure.UpyProperties;
import com.upyun.*;
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
public class UpyClient implements IFileUploader {

    @Autowired
    UpyProperties upyProperties;

    /**
     * 获取又拍云签名
     *
     * @param path 文件存储父路径
     * @return {ResultDTO}
     * @throws UpException 又拍云异常
     */
    public HashMap<String, String> getSign(String path) throws UpException {

        String date = DateUtil.getGmtString(new Date());
        String saveKey = path + "/{random32}.{suffix}";

        HashMap<String, Object> map = new HashMap<>(5);
        map.put(Params.BUCKET, upyProperties.getBucket());
        map.put(Params.SAVE_KEY, saveKey);
        map.put(Params.EXPIRATION, String.valueOf(System.currentTimeMillis() / 1000L + upyProperties.getExpiration()));
        map.put(Params.DATE, date);
        map.put(Params.CONTENT_MD5, "");

        String policy = UpYunUtils.getPolicy(map);
        String authorization = UpYunUtils.sign("POST", date, "/" + upyProperties.getBucket(),
                upyProperties.getOperator(), UpYunUtils.md5(upyProperties.getPassword()), policy);

        HashMap<String, String> retMap = new HashMap<>(4);
        retMap.put("authorization", authorization);
        retMap.put("policy", policy);
        retMap.put("date", date);
        retMap.put("serviceUrl", upyProperties.getServiceUrl() + upyProperties.getBucket());
        retMap.put("imageHost", upyProperties.getImageHost());
        return retMap;
    }

    /**
     * 上传base64图片
     *
     * @param path        文件存储父路径
     * @param base64Image base64图片
     * @return 图片绝对路径
     */
    public String uploadBase64Image(String path, String base64Image) {
        byte[] fileBytes = Base64Coder.decode(base64Image);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);

        String suffix = FileTypeUtil.getType(byteArrayInputStream);
        // 文件格式错误
        if (StringUtils.isEmpty(suffix)) {
            return null;
        }
        String saveKey = path + "/{random32}." + suffix;
        HashMap<String, Object> paramsMap = new HashMap<>(1);
        paramsMap.put(Params.SAVE_KEY, saveKey);
        try {
            FormUploader uploader = new FormUploader(upyProperties.getBucket(),
                    upyProperties.getOperator(), upyProperties.getPassword());
            Result result = uploader.upload(paramsMap, Base64Coder.decode(base64Image));
            if (result.isSucceed()) {
                JSONObject jsonObject = JSONUtil.parseObj(result.getMsg());
                String url = jsonObject.get("url", String.class);
                return upyProperties.getImageHost() + url;
            } else {
                return null;
            }
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传图片流
     *
     * @param path  文件存储父路径
     * @param bytes 文件字节
     * @return 图片绝对路径
     */
    public String uploadFile(String path, byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        String suffix = FileTypeUtil.getType(byteArrayInputStream);
        String saveKey = path + "/{random32}." + suffix;

        HashMap<String, Object> paramsMap = new HashMap<>(1);
        paramsMap.put(Params.SAVE_KEY, saveKey);

        try {
            FormUploader uploader = new FormUploader(upyProperties.getBucket(),
                    upyProperties.getOperator(), upyProperties.getPassword());
            Result result = uploader.upload(paramsMap, bytes);
            JSONObject jsonObject = JSONUtil.parseObj(result.getMsg());
            String url = jsonObject.get("url", String.class);
            return upyProperties.getImageHost() + url;
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadFile(String biz, MultipartFile file) throws IOException {
        return null;
    }

    @Override
    public String uploadImage(String biz, String base64Image) {
        return null;
    }

    @Override
    public byte[] readFile(String url) {
        return new byte[0];
    }

    @Override
    public String saveFile(String biz, byte[] fileBytes, String fileName) {
        return null;
    }
}
