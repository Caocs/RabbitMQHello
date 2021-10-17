package com.java.ccs.study.rabbitmq.demo.example03;

import com.java.ccs.study.rabbitmq.demo.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author caocs
 * @date 2021/10/10
 * 发布确认模式
 */
public class WorkProducer {

    public static void main(String[] args) throws Exception {
        singlePublishConfirm();
        multiPublishConfirm();
        multiPublishConfirmAsync();
    }

    /**
     * 单个确认模式
     */
    public static void singlePublishConfirm() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.confirmSelect(); // 开启发布确认
        channel.queueDeclare(queueName, true, false, false, null);
        // 模拟批量发送消息
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            String message = "" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 单个消息就马上发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                // System.out.println("消息发送成功：" + message);
            }
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("单个发布确认模式，消息发送完毕，耗时：" + time);
    }

    /**
     * 批量确认模式
     */
    public static void multiPublishConfirm() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.confirmSelect(); // 开启发布确认
        channel.queueDeclare(queueName, true, false, false, null);
        // 模拟批量发送消息
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            String message = "" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 模拟每100条确认一次。
            if (i % 100 == 0) {
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    // System.out.println("消息发送成功：" + message);
                }
            }
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("批量发布确认模式，消息发送完毕，耗时：" + time);
    }

    /**
     * 批量确认模式
     */
    public static void multiPublishConfirmAsync() throws Exception {
        long start = System.currentTimeMillis();
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.confirmSelect(); // 开启发布确认
        channel.queueDeclare(queueName, true, false, false, null);

        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况下
         *1、轻松将序号与消息进行关联
         * 2、轻松批量删除条目，只要给到序号。
         * 3、支持高并发
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        /**
         * 消息确认成功的回调函数
         * 1、消息的标记
         * 2、是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                // 删除掉已经确认的消息，剩下的就是未确认的消息。
                ConcurrentNavigableMap<Long, String> confirmed
                        = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
                System.out.println("multiple=true");
            } else {
                System.out.println("multiple=false");
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认成功的消息：" + deliveryTag);
        };
        // 消息确认失败的回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            // 3、打印一下未确认的消息都有哪些
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("确认失败的消息Tag：" + deliveryTag + " 消息：" + message);
        };
        // 消息监听器，监听哪些消息成功哪些失败。（异步通知）
        channel.addConfirmListener(ackCallback, nackCallback);
        // 模拟批量发送消息（这里不用再等待确认了，因为有异步监听器）
        for (int i = 1; i <= 1000; i++) {
            String message = "" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 1、此处记录下所有要发送的消息，消息的总和。
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("异步批量发布确认模式，消息发送完毕，耗时：" + time);
    }

}
