package com.java.ccs.study.rabbitmq.demo.example02;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author caocs
 * @date 2021/10/10
 * 工作线程生产者
 * 1、队列持久化
 * 2、消息持久化 MessageProperties.PERSISTENT_TEXT_PLAIN
 */
public class WorkProducer {

    public static void main(String[] args) throws Exception {
        final String QUEUE_NAME = "hello";
        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("创建一个队列，下面可以发送消息啦：");
        /**
         * 生成一个队列
         * 1、队列名称
         * 2、队列里面的消息是否持久化（默认情况下消息存放在内存中）
         * 3、该队列是否只供一个消费者进行消费（默认false）
         * 4、是否自动删除。最后一个消费者断开连接后，该队列是否自动删除。
         * 5、其他参数
         */
        boolean durable = true; //让队列持久化
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        // 为了方便模拟发送多条消息，从控制台中输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 发送一个消息
             * 1、发送到哪个交换机
             * 2、路由的key值是哪个，本次是队列的名称
             * 3、其他参数  MessageProperties.PERSISTENT_TEXT_PLAIN(消息持久化)
             * 4、发送消息的消息体
             *
             * 注：
             * 1、默认消息保存在内存中（不是持久化的）。PERSISTENT_TEXT_PLAIN后会保存在磁盘中（持久化）
             * 2、消息标记为持久化并不能完全保证消息不会丢失。
             */
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("消息发送完毕");
        }

    }

}
