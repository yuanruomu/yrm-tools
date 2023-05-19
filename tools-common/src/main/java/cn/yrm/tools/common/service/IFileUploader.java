package cn.yrm.tools.common.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileUploader {

    /**
     * 上传文件
     *
     * @param biz
     * @param file
     * @return 地址全路径
     */
    String uploadFile(String biz, MultipartFile file) throws IOException;

    /**
     * 上传Base64图片
     *
     * @param base64Image
     * @return 地址全路径
     */
    String uploadImage(String biz, String base64Image);

    /**
     * 读取文件
     *
     * @param url
     * @return
     */
    byte[] readFile(String url);

    /**
     * 保存文件
     *
     * @param biz
     * @param fileBytes
     * @param fileName
     * @return
     */
    String saveFile(String biz, byte[] fileBytes, String fileName);
}
