package cn.edu.xmu.mini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"cn.edu.xmu.mini.core",
        "cn.edu.xmu.mini.config", "cn.edu.xmu.mini.goods", "cn.edu.xmu.mini.orders", "cn.edu.xmu.mini.shop"})
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}
