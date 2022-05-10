package cn.edu.xmu.mini.core.util.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wishes.storage")
@Data
public class StorageProperties {
    private String active;
    private Local local;
    private Webdav webdav;

    @Data
    public static class Local {
        private String address;
        private String storagePath;
    }

    @Data
    public static class Webdav {
        private String url;
        private String directory;
        private String username;
        private String password;
    }
}
