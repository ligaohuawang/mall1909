package com.qf.entity.mall_back;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.qf")
@Import(FdfsClientConfig.class)
public class MallBackApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallBackApplication.class, args);
    }

}
