package com.java.ccs.study.rabbitmq.demo.example09;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author caocs
 * @date 2021/10/16
 * 死信队列
 * 消费死信消息
 */
public class DeadConsumer02 {

    public static void main(String[] args) throws Exception {
        final String deadQueue = "dead_queue";
        System.out.println("死信队列的消费者，等待接收消息...");
        Channel channel = RabbitMQUtil.getChannel();
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("死信队列的消费者:你怎么跑我这来了：" + new String(message.getBody()));
            System.out.println(message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = (message) -> {
        };
        channel.basicConsume(deadQueue, true, deliverCallback, cancelCallback);
    }


}
