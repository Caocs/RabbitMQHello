package com.java.ccs.study.rabbitmq.demo.example05;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author caocs
 * @date 2021/10/10
 * direct类型交换机（路由模式）
 * 在消费数据时，需要交换机和队列绑定。
 */
public class DirectConsumer02 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 声明一个交换机（direct类型）
        final String exchangeName = "DirectExchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        final String queueName = "Disk";
        channel.queueDeclare(queueName, true, false, false, null);
        // 分别绑定队列（不同的routingKey）
        channel.queueBind(queueName, exchangeName, "error");
        System.out.println("DirectConsumer02 等待接收消息...");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("DirectConsumer01:" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (message) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }

}
