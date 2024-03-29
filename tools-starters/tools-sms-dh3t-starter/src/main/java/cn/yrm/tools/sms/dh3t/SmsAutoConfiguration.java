package cn.yrm.tools.sms.dh3t;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "dh3t.sms.key")
public class SmsAutoConfiguration {
    @Value("${dh3t.sms.host:http://www.dh3t.com}")
    private String host;
    @Value("${dh3t.sms.key}")
    private String accessKeyId;
    @Value("${dh3t.sms.secret}")
    private String accessKeySecret;
    @Value("${dh3t.sms.sign}")
    private String signName;
    @Value("${dh3t.sms.mock:false}")
    private boolean mock;

    @Bean
    public Dh3tMessageClient getMessageClient() {
        return new Dh3tMessageClient(host, accessKeyId, accessKeySecret, signName, mock);
    }
}
