package com.yrm.tools.alioss.autoconfigure;

import com.yrm.tools.alioss.client.AliOssClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliOssProperties.class)
@ConditionalOnProperty(name = "aliyun.oss.bucket")
public class AliOssAutoConfiguration {

    @Autowired
    AliOssProperties aliOssProperties;
    @Bean
    public AliOssClient getOssClient() {
        return new AliOssClient(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucket());
    }
}
