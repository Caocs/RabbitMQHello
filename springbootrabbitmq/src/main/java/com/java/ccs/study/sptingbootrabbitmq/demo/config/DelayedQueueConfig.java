package com.java.ccs.study.sptingbootrabbitmq.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caocs
 * @date 2021/10/16
 */
@Configuration
public class DelayedQueueConfig {
    /**
     * 基于延时队列插件，声明延时队列交换机 exchange_delayed
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        /**
         * 1.交换机的名称
         * 2.交换机的类型
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其他参数
         */
        return new CustomExchange("exchange_delayed", "x-delayed-message",
                true, false, arguments);
    }

    /**
     * 普通队列
     */
    @Bean
    public Queue delayedQueue() {
        return new Queue("queue_delayed");
    }

    @Bean
    public Binding queueBindingExchangeDelayed(
            Queue delayedQueue,
            CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange)
                .with("routing-key.delayed").noargs();
    }

}
