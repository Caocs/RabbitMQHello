package com.java.ccs.study.sptingbootrabbitmq.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author caocs
 * @date 2021/10/16
 */
@Slf4j
@Component
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void postInit() {
        // 在RabbitTemplate注入后执行，把该类MyCallback对象set到单例RabbitTemplate中。
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1.发消息，交换机接收到了，回调
     * * 1.1 correlationData 保存回调消息的ID及相关信息
     * * 1.2 交换机收到消息 ack=true
     * * 1.3 cause null
     * 2.发消息，交换机接收失败，回调
     * * 2.1 correlationData 保存回调消息的ID及相关信息
     * * 2.2 交换机收到消息 ack=false
     * * 2.3 cause 失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData == null ? "" : correlationData.getId();
        if (ack) {
            log.info("交换机已经收到 id 为:{}的消息", id);
        } else {
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}", id, cause);
        }
    }

    /**
     * 可以在消息传递过程中不可达目的地时，将消息返回给生产者。
     * （只有不可达的时候才会回退）
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        String msg = new String(returnedMessage.getMessage().getBody());
        String routingKey = returnedMessage.getRoutingKey();
        String exchange = returnedMessage.getExchange();
        log.info("消息未发送出去，被退货了。交换机：{}，路由：{}，消息：{}", exchange, routingKey, msg);
    }
}
