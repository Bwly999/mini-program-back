package cn.edu.xmu.mini.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "cn.edu.xmu.mini.*.dao")
public class MongoRepositoryConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "mini";
    }
}
