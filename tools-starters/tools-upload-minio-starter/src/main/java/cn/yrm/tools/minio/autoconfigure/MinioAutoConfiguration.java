package cn.yrm.tools.minio.autoconfigure;

import cn.yrm.tools.common.service.IFileUploader;
import cn.yrm.tools.minio.client.MinioOssClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置
 *
 * @author XUQ
 */
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@ConditionalOnProperty(name = "minio.bucket")
public class MinioAutoConfiguration {

    @Autowired
    MinioProperties minioProperties;

    @Bean
    public IFileUploader getMinioClient() {
        return new MinioOssClient(minioProperties.getEndpoint(),
                minioProperties.getAccessKeyId(), minioProperties.getAccessKeySecret());
    }
}