package com.java.ccs.study.rabbitmq.demo.example06;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author caocs
 * @date 2021/10/10
 * <p>
 * 消费者队列
 * Q1：routingKey: #.basic，info.*
 * Q2：routingKey：error.*
 * 则在生产数据时，会根据routingKey进行匹配，需要发送的队列
 */
public class TopicProducer {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 声明一个交换机（topic类型）（需要和消费者中交换机名称一致）
        final String exchangeName = "TopicExchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(exchangeName, "error.basic", null, message.getBytes());
            System.out.println("消息发送完毕");
        }
    }

}
