package com.java.ccs.study.sptingbootrabbitmq.demo.consumer;

import com.java.ccs.study.sptingbootrabbitmq.demo.config.ConfirmConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author caocs
 * @date 2021/10/16
 */
@Slf4j
@Component
public class ConfirmConsumer {

    /**
     * 模拟confirm.queue队列正常收到消息
     */
    @RabbitListener(queues = "confirm.queue")
    public void receiveA(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到confirm.queue队列消息：{}", new Date().toString(), msg);
    }

    /**
     * 接收报警消息的队列
     */
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarning(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，警报警报，发现不可路由消息，消息：{}", new Date().toString(), msg);
    }


}
