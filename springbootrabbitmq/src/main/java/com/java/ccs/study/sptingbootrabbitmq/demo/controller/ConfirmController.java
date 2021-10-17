package com.java.ccs.study.sptingbootrabbitmq.demo.controller;

import com.java.ccs.study.sptingbootrabbitmq.demo.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author caocs
 * @date 2021/10/16
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 模拟交换机没有收到消息，然后通过回调方式发送通知
     * 1、需要配置spring.rabbitmq.publisher-confirm-type=correlated
     * 2、定义回调方法MyCallback并注入
     * 3、发送消息时convertAndSend，传入CorrelationData。
     */
    @GetMapping("/sendmsg/{message}")
    public void sendMessage(@PathVariable String message) {
        log.info("发送消息：{}", message);
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME + "123", // 为了模拟交换机异常，把名称改错
                "key1",
                message,
                correlationData
        );

        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                "key1" + "123", // 为了模拟队列未收到消息的异常，把名称改错
                message,
                correlationData
        );
    }

}
