package cn.edu.xmu.mini.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mini.weixin")
@Data
public class WeixinConfig {
    private String appId;

    private String appSecret;
}
