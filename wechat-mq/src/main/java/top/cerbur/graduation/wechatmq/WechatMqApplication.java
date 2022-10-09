package top.cerbur.graduation.wechatmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = "top.cerbur.graduation")
public class WechatMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatMqApplication.class, args);
    }
}
