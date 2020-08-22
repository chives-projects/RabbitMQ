package com.csc.rabbitmqdemo.tradition;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 这是java原生类支持RabbitMQ，直接运行该类
 */
public class DistributionSender {

    private final static String QUEUE_NAME = "test";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * 创建连接连接到RabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();

        // 设置RabbitMQ所在主机ip或者主机名
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("192.168.160.22");
        factory.setVirtualHost("/");
        factory.setPort(5672);

        // 创建一个连接
        Connection connection = factory.newConnection();

        // 创建一个频道
        Channel channel = connection.createChannel();
//        channel.exchangeDeclare("name",BuiltinExchangeType.DIRECT);

        Map<String, Object> arg = new HashMap<>();
        arg.put("x-max-priority", 3);
        arg.put("x-max-length", 3);
        arg.put("x-max-length-bytes", 1024);
        /**
         * queue溢出行为，这将决定当队列达到设置的最大长度或者最大的存储空间时发送到消息队列的消息的处理方式；
         * 有效的值是：
         * drop-head（删除queue头部的消息）、
         * reject-publish（最近发来的消息将被丢弃）、
         * reject-publish-dlx（拒绝发送消息到死信交换器）
         * 类型为quorum 的queue只支持drop-head;
         */
        arg.put("x-over-flow", "reject-publish-dlx");
//        arg.put("x-queue-mode", "lazy");
        arg.put("x-message-ttl", 5000);
//        arg.put("x-expires", 6000); // 队列经过多长时间会删除
        arg.put("x-dead-letter-exchange", "topicExchange"); // 消息被拒绝或过期时重新发送到的交换机

        // 指定一个队列
        boolean durable = false;
        boolean autoDelete = false;
        /**
         一：当连接关闭时connection.close()该队列是否会自动删除；
         二：该队列是否是私有的private，如果不是排外的，可以使用两个消费者都访问同一个队列，没有任何问题;
         如果是排外的，会对当前队列加锁，其他通道channel是不能访问的，如果强制访问会报异常：
         com.rabbitmq.client.ShutdownSignalException: channel error; protocol method:
         #method<channel.close>(reply-code=405, reply-text=RESOURCE_LOCKED -
         cannot obtain exclusive access to locked queue 'queue_name' in vhost '/', class-id=50, method-id=20)
         一般等于true的话用于一个队列只能有一个消费者来消费的场景
         */
        boolean exclusive = false;
//        boolean noWait = false;
        channel.queueDeclare(QUEUE_NAME, durable, autoDelete, exclusive, arg);

        // 限制发给同一个消费者不得超过1条消息
//        channel.basicQos(1);
        channel.addReturnListener((returnMessage) -> {
            System.out.println(returnMessage);
            System.out.println("监听器执行");
        });

        for (int i = 0; i < 8; i++) {
            AMQP.BasicProperties.Builder properties = MessageProperties.PERSISTENT_TEXT_PLAIN.builder();
            properties.messageId("消息ID");
            properties.deliveryMode(2);
            properties.priority(1);
            properties.expiration("10000");
            properties.contentType("text/plain");

            String message = "This is a task, and the complexity is " + i + "。";
//            String message = "This is a task, and the complexity is 。";

            channel.basicPublish("", QUEUE_NAME, true, properties.build(), message.getBytes());
//			channel.basicPublish("", QUEUE_NAME, properties.build(), message.getBytes());
            System.out.println(" DistributionSender2 Sent '" + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}
