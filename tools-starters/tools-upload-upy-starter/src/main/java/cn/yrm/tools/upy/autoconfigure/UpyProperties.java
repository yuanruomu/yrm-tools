package cn.yrm.tools.upy.autoconfigure;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 自动配置属性
 */
@ConfigurationProperties(prefix = "upy")
@Getter
@Setter
public class UpyProperties {

    /**
     * 服务名
     */
    private String bucket;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 操作员密钥
     */
    private String password;

    /**
     * 图片服务域名
     */
    private String imageHost;

    /**
     * 又拍云服务API地址
     */
    private String serviceUrl = "https://v0.api.upyun.com/";

    /**
     * 默认过期时间
     */
    private long expiration = 1800;
}