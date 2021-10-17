# RabbitMQHello
学习RabbitMQ时写的一些Demo

## 1、前言

根据b站上尚硅谷出版RabbitMQ【<https://www.bilibili.com/video/BV1cb4y1o7zz>】，写的代码。如有侵权，望告知我会删除。

## 2、目录

### rabbitmqdemo文件夹

（1）example01

​	基本生产消费队列

（2）example02

​	工作模式，

​	测试队列持久化、消息持久化、自动/手动应答等场景。

（3）example03

​	发布确认模式

​	模拟单个确认、批量确认、异步批量确认等场景

（4）example04

​	发布订阅模式（fanout）

（5）example05

​	路由模式（direct）

（6）example06

​	主题模式（topic）

（7）example07

​	死信队列（超过存活时间TTL的情况）

（8）example08

​	死信队列（超过队列最大长度的情况）

（9）example09

​	死信队列（消息被拒绝且requeue=false的情况）

（10）example10

​	优先级队列

### springbootrabbitmq文件夹

通过Springboot整合RabbitMQ的Demo。

（1）基于死信队列（超过TTL）的方式实现延迟队列

​	存在前一个消息存活时间过长导致下一个不能被消费到的问题。

（2）基于rabbitmq_delayed_message_exchange插件实现延迟队列

（3）发布确认（消息投递失败）场景

​	ConfirmCallback、ReturnCallback

（4）备份队列



