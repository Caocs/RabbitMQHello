package com.java.ccs.study.rabbitmq.demo.example06;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author caocs
 * @date 2021/10/10
 */
public class TopicConsumer01 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 声明一个交换机（direct类型）
        final String exchangeName = "TopicExchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
        final String queueName = "queue1";
        channel.queueDeclare(queueName, true, false, false, null);
        // 分别绑定队列（不同的routingKey）
        channel.queueBind(queueName, exchangeName, "#.basic");
        channel.queueBind(queueName, exchangeName, "info.*");
        System.out.println("TopicConsumer01 等待接收消息...");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("TopicConsumer01:" + new String(message.getBody()));
            System.out.println(message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = (message) -> {
        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }

}
