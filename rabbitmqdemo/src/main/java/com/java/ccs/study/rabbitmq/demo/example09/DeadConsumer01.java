package com.java.ccs.study.rabbitmq.demo.example09;

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
 * 模拟消息被拒绝且requeue=false的情况。
 * 注意此处必须修改为手动应答。
 * channel.basicConsume(normalQueue, false, deliverCallback, cancelCallback);
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
            String msg = new String(message.getBody());
            if ("info5".equals(msg)) {
                System.out.println("我拒绝你");
                // 模拟拒绝消息并且不放回队列的情况。
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("普通队列的消费者:" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        CancelCallback cancelCallback = (message) -> {
        };
        // 注意此处必须修改为手动应答。
        channel.basicConsume(normalQueue, false, deliverCallback, cancelCallback);
    }


}
