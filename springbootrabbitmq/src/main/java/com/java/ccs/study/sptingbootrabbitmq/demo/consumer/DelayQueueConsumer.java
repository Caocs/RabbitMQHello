package com.java.ccs.study.sptingbootrabbitmq.demo.consumer;

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
public class DelayQueueConsumer {

    /**
     * 模拟被queue_d_dead死信队列收到消息。
     * 由于没有队列queue_b在接收消息，时间ttl超时则转到该死信队列
     */
    @RabbitListener(queues = {"queue_delayed"})
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到queue_delayed延迟队列消息：{}", new Date().toString(), msg);
    }


}
