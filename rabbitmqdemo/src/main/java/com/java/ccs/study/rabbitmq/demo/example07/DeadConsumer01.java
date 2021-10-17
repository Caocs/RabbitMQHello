package com.java.ccs.study.rabbitmq.demo.example07;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caocs
 * @date 2021/10/16
 * 死信队列
 * 正常消费队列
 */
public class DeadConsumer01 {

    public static void main(String[] args) throws Exception {
        final String normalExchange = "Normal_Exchange";
        final String deadExchange = "Dead_Exchange";
        final String normalQueue = "normal_queue";
        final String deadQueue = "dead_queue";
        System.out.println("普通队列的消费者，等待接收消息...");

        Channel channel = RabbitMQUtil.getChannel();
        // 声明普通交换机和死信交换机，类型为Direct。
        channel.exchangeDeclare(normalExchange, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(deadExchange, BuiltinExchangeType.DIRECT);
        // 声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        // arguments.put("x-message-ttl",10000); // 消息过期时间ttl
        arguments.put("x-dead-letter-exchange", deadExchange); // 设置该队列的死信交换机
        arguments.put("x-dead-letter-routing-key", "dead-routing-key"); // 设置该队列的死信交换机的routingKey
        channel.queueDeclare(normalQueue, false, false, false, arguments);
        // 声明死信队列
        channel.queueDeclare(deadQueue, false, false, false, null);
        // 把普通队列和普通交换机进行绑定
        channel.queueBind(normalQueue, normalExchange, "normal-routing-key");
        // 把死信队列和死信交换机进行绑定
        channel.queueBind(deadQueue, deadExchange, "dead-routing-key");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("普通队列的消费者:" + new String(message.getBody()));
            System.out.println(message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = (message) -> {
        };
        channel.basicConsume(normalQueue, true, deliverCallback, cancelCallback);
    }


}
