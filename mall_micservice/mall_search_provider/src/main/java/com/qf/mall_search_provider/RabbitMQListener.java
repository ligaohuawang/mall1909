package com.qf.mall_search_provider;

import com.qf.entity.Goods;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 1.加@Component注解，让这个类被Spring扫到
 */
@Component
public class RabbitMQListener {
    /**
     * 2.@RabbitListener表示正在做一个监听，
     * 监听什么呢？监听search_queue队列。@QueueBinding，表示监听的同时
     * 在做一个绑定，绑定交换机lgh和队列search_queue
     *
     * @param goods
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "lgh", type = "fanout"),
            value = @Queue(name = "search_queue")
    ))
    public void msgHandler(Goods goods) {
        System.out.println("队列从交换机中拿到的消息：" + goods);
    }
}
