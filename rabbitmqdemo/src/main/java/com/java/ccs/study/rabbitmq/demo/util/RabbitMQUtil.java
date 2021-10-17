package com.java.ccs.study.rabbitmq.demo.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author caocs
 * @date 2021/10/10
 */
public class RabbitMQUtil {

    /**
     * 得到一个Connection的Channel
     */
    public static Channel getChannel() throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂IP，连接RabbitMQ的队列
        factory.setHost("192.168.18.1");
        // 使用guest用户会报错。
        factory.setUsername("admin");
        factory.setPassword("admin");
        // 创建连接
        Connection connection = factory.newConnection();
        // 获取信道
        return connection.createChannel();
    }


}
