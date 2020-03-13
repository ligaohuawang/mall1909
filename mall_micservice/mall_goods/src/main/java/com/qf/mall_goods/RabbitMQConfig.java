package com.qf.mall_goods;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//加上@Configuration表示这个类是配置相关的类
@Configuration
public class RabbitMQConfig {
    /**
     * 往Spring容器里面注册一个交换机，
     * 加@Bean注解表示将这个方法的返回值注册到SpringBoot容器里面去
     */
    @Bean("啊猫")
    public FanoutExchange getFanoutExchange() {
        return new FanoutExchange("lgh");
    }
}
