package top.cerbur.graduation.wechatapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(scanBasePackages = "top.cerbur.graduation")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WechatApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatApiApplication.class, args);
    }

}
