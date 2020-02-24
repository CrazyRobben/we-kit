package net.iotsite.wekit.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"net.iotsite.wekit.message", "net.iotsite.wekit.api"})
public class WechatMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatMessageApplication.class, args);
    }

}
