package cn.yrm.tools.minio.autoconfigure;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 自动配置属性
 */
@ConfigurationProperties(prefix = "minio")
@Getter
@Setter
public class MinioProperties {
    /**
     * 服务名
     */
    private String bucket;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String imageHost;
}