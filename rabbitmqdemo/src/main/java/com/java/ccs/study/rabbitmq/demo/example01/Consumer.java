package com.java.ccs.study.rabbitmq.demo.example01;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author caocs
 * @date 2021/10/10
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        final String QUEUE_NAME = "hello";
        Channel channel = RabbitMQUtil.getChannel();
        // 声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = (message) -> {
            System.out.println("消息消费被中断");
        };
        /**
         * 消费者消费消息：
         * 1、消费哪个队列
         * 2、消费成功后是否自动应答
         * 3、消费者未成功消费的回调
         * 4、消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }


}
