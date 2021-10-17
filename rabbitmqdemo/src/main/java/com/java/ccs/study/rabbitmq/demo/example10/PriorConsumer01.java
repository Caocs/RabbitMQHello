package com.java.ccs.study.rabbitmq.demo.example10;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author caocs
 * @date 2021/10/17
 */
public class PriorConsumer01 {

    public static void main(String[] args) throws Exception {
        System.out.println("优先级队列接收消息...");
        Channel channel = RabbitMQUtil.getChannel();
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消息:" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (message) -> {
        };
        channel.basicConsume("prior-queue", true, deliverCallback, cancelCallback);
    }

}
