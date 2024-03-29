package cn.yrm.tools.aliyun.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "aliyun.sms.key")
public class SmsAutoConfiguration {

    @Value("${aliyun.sms.key:}")
    private String accessKeyId;
    @Value("${aliyun.sms.secret:}")
    private String accessKeySecret;
    @Value("${aliyun.sms.sign:}")
    private String signName;
    @Value("${aliyun.sms.mock:false}")
    private boolean mock;

    @Bean
    public AliMessageClient getMessageClient() {
        return new AliMessageClient(accessKeyId, accessKeySecret, signName, mock);
    }
}
