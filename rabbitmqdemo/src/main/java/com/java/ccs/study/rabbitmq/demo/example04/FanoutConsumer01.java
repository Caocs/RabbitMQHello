package com.java.ccs.study.rabbitmq.demo.example04;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author caocs
 * @date 2021/10/10
 * fanout类型交换机（发布订阅模式）
 * 在消费数据时，需要交换机和队列绑定。
 */
public class FanoutConsumer01 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 声明一个交换机（fanout类型）
        final String exchangeName = "fanoutExchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
        /**
         * 声明一个队列（临时队列）
         * 队列的名称是随机的
         * 当消费者断开与队列的连接时，队列就自动删除。
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         * routingKey为空串
         */
        channel.queueBind(queueName, exchangeName, "");
        System.out.println("FanoutConsumer01 等待接收消息...");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("FanoutConsumer01:" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (message) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }

}
