package com.java.ccs.study.rabbitmq.demo.example10;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caocs
 * @date 2021/10/17
 *
 * 优先级队列
 */
public class PriorProducer {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10); // 设置最大优先级为10（官方可以0-255）
        channel.queueDeclare("prior-queue", true, false, false, arguments);
        // 模拟发送不同优先级的消息
        for (int i = 1; i <= 10; i++) {
            String msg = "info-" + i;
            if (i % 3 == 0) {
                // 对消息设置优先级为5.
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", "prior-queue", properties, msg.getBytes());
            } else {
                channel.basicPublish("", "prior-queue", null, msg.getBytes());
            }
        }
    }

}
