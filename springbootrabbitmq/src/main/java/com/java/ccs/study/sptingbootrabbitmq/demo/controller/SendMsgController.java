package com.java.ccs.study.sptingbootrabbitmq.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author caocs
 * @date 2021/10/16
 * 发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 队列上设置延迟时间
     */
    @GetMapping("/sendmsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给两个TTL队列", new Date().toString(), message);
        // 通过一个交换机发送两条不同的路由消息。会分别被两个队列收到消息
        rabbitTemplate.convertAndSend("exchange_x", "XA", "消息来自ttl=10s的队列" + message);
        rabbitTemplate.convertAndSend("exchange_x", "XB", "消息来自ttl=40s的队列" + message);
    }

    /**
     * 设置消息的延迟时间
     * 注意：
     * 存在第一个消息延迟时间很长，而第二个消息时间很短，
     * 但是第二个消息并不会优先执行的问题。
     */
    @GetMapping("/sendttlmsg/{ttl}/{message}")
    public void sendTtlMsg(@PathVariable String ttl, @PathVariable String message) {
        log.info("当前时间：{},发送一条信息给queueC队列：{},时长{}", new Date().toString(), message, ttl);
        // 通过一个交换机发送两条不同的路由消息。会分别被两个队列收到消息
        rabbitTemplate.convertAndSend(
                "exchange_x",
                "XC",
                message,
                msg -> {
                    // 设置消息的延迟时间
                    msg.getMessageProperties().setExpiration(ttl);
                    return msg;
                }
        );
    }


    /**
     * 基于插件的发送延迟消息
     */
    @GetMapping("/senddelayedmsg/{ttl}/{message}")
    public void sendDelayedMsg(@PathVariable String ttl, @PathVariable String message) {
        log.info("当前时间：{},发送一条信息给queue_delayed队列：{},时长{}", new Date().toString(), message, ttl);
        // 通过一个交换机发送两条不同的路由消息。会分别被两个队列收到消息
        rabbitTemplate.convertAndSend(
                "exchange_delayed",
                "routing-key.delayed",
                message,
                msg -> {
                    // 设置消息的延迟时间
                    msg.getMessageProperties().setExpiration(ttl);
                    return msg;
                }
        );
    }

}
