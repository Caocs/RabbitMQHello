package com.java.ccs.study.sptingbootrabbitmq.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caocs
 * @date 2021/10/16
 */
@Configuration
public class TtlQueueConfig {

    /**
     * 声明普通交换机 x_exchange
     */
    @Bean("exchangeX")
    public DirectExchange exchangeX() {
        return new DirectExchange("exchange_x");
    }

    /**
     * 声明普通交换机 y_dead_exchange
     */
    @Bean("exchangeYDead")
    public DirectExchange exchangeYDead() {
        return new DirectExchange("exchange_y_dead");
    }

    /**
     * 声明普通队列
     * 设置ttl为10s。
     */
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000); // 消息过期时间ttl=10s
        arguments.put("x-dead-letter-exchange", "exchange_y_dead"); // 设置该队列的死信交换机
        arguments.put("x-dead-letter-routing-key", "YD"); // 设置该队列的死信交换机的routingKey
        return QueueBuilder.durable("queue_a")
                .withArguments(arguments)
                .build();
    }

    /**
     * 声明普通队列
     * 设置ttl为40s。
     */
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 40000); // 消息过期时间ttl=40s
        arguments.put("x-dead-letter-exchange", "exchange_y_dead"); // 设置该队列的死信交换机
        arguments.put("x-dead-letter-routing-key", "YD"); // 设置该队列的死信交换机的routingKey
        return QueueBuilder.durable("queue_b")
                .withArguments(arguments)
                .build();
    }

    /**
     * 声明普通队列
     * 不设置超时时间的通用队列
     */
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "exchange_y_dead"); // 设置该队列的死信交换机
        arguments.put("x-dead-letter-routing-key", "YD"); // 设置该队列的死信交换机的routingKey
        return QueueBuilder.durable("queue_c")
                .withArguments(arguments)
                .build();
    }

    /**
     * 声明死信队列
     */
    @Bean("queueDDead")
    public Queue queueDeadD() {
        return QueueBuilder.durable("queue_d_dead").build();
    }

    @Bean
    public Binding queueABindingExchangeX(
            Queue queueA,
            DirectExchange exchangeX) {
        return BindingBuilder.bind(queueA).to(exchangeX).with("XA");
    }

    @Bean
    public Binding queueBBindingExchangeX(
            Queue queueB,
            DirectExchange exchangeX) {
        return BindingBuilder.bind(queueB).to(exchangeX).with("XB");
    }

    @Bean
    public Binding queueCBindingExchangeX(
            Queue queueC,
            DirectExchange exchangeX) {
        return BindingBuilder.bind(queueC).to(exchangeX).with("XC");
    }

    @Bean
    public Binding queueDBindingExchangeY(
            Queue queueDDead,
            DirectExchange exchangeYDead) {
        // 注意此处的路由需要和上面设置的死信路由一致
        return BindingBuilder.bind(queueDDead).to(exchangeYDead).with("YD");
    }

}
