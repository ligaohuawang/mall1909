package com.qf.mall_search_provider;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.qf")
@DubboComponentScan("service")
public class MallSearchProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallSearchProviderApplication.class, args);
    }

}
