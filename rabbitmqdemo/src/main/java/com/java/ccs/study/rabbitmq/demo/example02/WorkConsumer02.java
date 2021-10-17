package com.java.ccs.study.rabbitmq.demo.example02;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.java.ccs.study.rabbitmq.demo.util.SleepUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author caocs
 * @date 2021/10/10
 * 模拟多个工作线程处理消息，并且手动消息应答。
 * 1、消息只会被一个工作线程消费
 * 2、手动应答：如果一个消息没有被ACK应答，会被其他工作线程消费执行。不会消息丢失。
 * 3、不公平分发：channel.basicQos(1); 默认为0，轮训分发
 */
public class WorkConsumer02 {

    public static void main(String[] args) throws Exception {
        final String QUEUE_NAME = "hello";
        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("C2等待接收消息......");
        // 声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("正在处理消息，但是耗时很长...");
            SleepUtil.sleep(20);
            System.out.println(consumerTag + "接收到的消息：" + new String(message.getBody()));
            /**
             * 当接收完消息后，手动应答。
             * 1、消息标记tag
             * 2、是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息消费被中断");
        };
        /**
         * 消费者消费消息：
         * 1、消费哪个队列
         * 2、消费成功后是否自动应答
         * 3、消费者未成功消费的回调
         * 4、消费者取消消费的回调
         */
        boolean autoAck = false; // 手动应答
        channel.basicQos(2); // 不公平分发:1，预取值:>1
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);

    }

}
