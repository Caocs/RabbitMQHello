package com.java.ccs.study.rabbitmq.demo.example05;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author caocs
 * @date 2021/10/10
 */
public class DirectProducer {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 声明一个交换机（direct类型）（需要和消费者中交换机名称一致）
        final String exchangeName = "DirectExchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(exchangeName, "info", null, message.getBytes());
            System.out.println("消息发送完毕");
        }

    }

}
