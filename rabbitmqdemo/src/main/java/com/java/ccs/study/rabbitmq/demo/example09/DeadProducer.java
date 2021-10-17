package com.java.ccs.study.rabbitmq.demo.example09;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * @author caocs
 * @date 2021/10/16
 */
public class DeadProducer {

    /**
     * 模拟死信队列的生产者
     * 只需要发送消息给Normal_Exchange交换机
     */
    public static void main(String[] args) throws Exception {
        final String normalExchange = "Normal_Exchange";
        System.out.println("生产者，等待接收消息...");
        Channel channel = RabbitMQUtil.getChannel();
        // 声明普通交换机，类型为Direct。
        channel.exchangeDeclare(normalExchange, BuiltinExchangeType.DIRECT);
        // 模拟发送10条消息
        for (int i = 1; i <= 10; i++) {
            String message = "info" + i;
            channel.basicPublish(normalExchange, "normal-routing-key", null, message.getBytes());
        }
    }

}
