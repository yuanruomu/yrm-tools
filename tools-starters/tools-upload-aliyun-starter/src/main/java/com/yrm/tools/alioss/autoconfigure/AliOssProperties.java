package com.yrm.tools.alioss.autoconfigure;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 自动配置属性
 */
@ConfigurationProperties(prefix = "aliyun.oss")
@Getter
@Setter
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
}