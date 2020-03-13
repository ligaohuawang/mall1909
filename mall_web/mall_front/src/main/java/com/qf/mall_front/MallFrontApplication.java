package com.qf.mall_front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.qf")
public class MallFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallFrontApplication.class, args);
    }

}
