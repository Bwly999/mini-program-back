package cn.edu.xmu.mini.goods.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mini.goods")
@Data
public class CategoryProperty {
    private List<String> categoryList;
}
