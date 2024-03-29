package cn.yrm.tools.upy.autoconfigure;

import cn.yrm.tools.common.service.IFileUploader;
import cn.yrm.tools.upy.client.UpyClient;
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
@EnableConfigurationProperties(UpyProperties.class)
@ConditionalOnProperty(name = "upy.bucket")
public class UpyAutoConfiguration {

    @Bean
    public IFileUploader getUpyClient() {
        return new UpyClient();
    }
}