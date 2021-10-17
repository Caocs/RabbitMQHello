package com.java.ccs.study.rabbitmq.demo.example01;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;

/**
 * @author caocs
 * @date 2021/10/10
 * 简单队列模式
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        final String QUEUE_NAME = "hello";
        Channel channel = RabbitMQUtil.getChannel();
        /**
         * 生成一个队列
         * 1、队列名称
         * 2、队列里面的消息是否持久化（默认情况下消息存放在内存中）
         * 3、该队列是否只供一个消费者进行消费（默认false）
         * 4、是否自动删除。最后一个消费者断开连接后，该队列是否自动删除。
         * 5、其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "hello world";
        /**
         * 发送一个消息
         * 1、发送到哪个交换机
         * 2、路由的key值是哪个，本次是队列的名称
         * 3、其他参数
         * 4、发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");

    }

}
