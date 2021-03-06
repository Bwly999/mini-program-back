package cn.edu.xmu.mini.core.util.storage.config;

import cn.edu.xmu.mini.core.util.storage.LocalStorage;
import cn.edu.xmu.mini.core.util.storage.Storage;
import cn.edu.xmu.mini.core.util.storage.StorageUtil;
import cn.edu.xmu.mini.core.util.storage.WebDavStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

    private final StorageProperties properties;

    public StorageAutoConfiguration(StorageProperties properties) {
        this.properties = properties;
    }

    @Bean
    public StorageUtil storageUtil() {
        String active = this.properties.getActive();
        if (active == null) {
            return null;
        }
        StorageUtil storageUtil = new StorageUtil();
        storageUtil.setActive(active);
        if (active.equals("local")) {
            storageUtil.setStorage(localStorage());
        }
        else if (active.equals("webdav")) {
            storageUtil.setStorage(webDavStorage());
        } else {
            throw new RuntimeException("当前存储模式 " + active + " 不支持");
        }

        return storageUtil;
    }

    @Bean
    @ConditionalOnProperty(prefix = "wishes.storage", name = "active", havingValue = "local")
    public Storage localStorage() {
        LocalStorage localStorage = new LocalStorage();
        StorageProperties.Local local = this.properties.getLocal();
        localStorage.setAddress(local.getAddress());
        localStorage.setStoragePath(local.getStoragePath());
        return localStorage;
    }

    @Bean
    @ConditionalOnProperty(prefix = "wishes.storage", name = "active", havingValue = "webdav")
    public WebDavStorage webDavStorage() {
        StorageProperties.Webdav webdav = this.properties.getWebdav();
        return new WebDavStorage(webdav.getUrl(), webdav.getDirectory(), webdav.getUsername(), webdav.getPassword());
    }
}
