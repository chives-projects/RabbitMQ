package com.csc.rabbitmqdemo.tradition;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * 这是java原生类支持RabbitMQ，直接运行该类
 */
public class DistributionReceiver {

    private final static String QUEUE_NAME = "test";

    public static void main(String[] argv) throws IOException, InterruptedException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("192.168.160.22");
        factory.setVirtualHost("/");
        factory.setPort(5672);
        // 打开连接和创建频道，与发送端一样

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("DistributionReceiver2 waiting for messages. To exit press CTRL+C");

        // 创建队列消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" DistributionReceiver2  : " + message);
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
                System.out.println(" Proccessing2... at " + time.format(new Date()));

                String event = envelope.getRoutingKey();
                System.out.println(event);
//                if (event.equals("queue.created")){
//                    System.out.println("created");
//                }else {
//                    System.out.println("del");
//                }
                try {
                    for (char ch : message.toCharArray()) {
                        if (ch == '.') {
                            doWork(1000);
                        }
                    }
                } catch (InterruptedException e) {
                } finally {
                    System.out.println(" DistributionReceiver2 Done! at " + time.format(new Date()));
//                    channel.basicAck(envelope.getDeliveryTag(),true);
                }
            }
        };
        // 开启自动确认
        boolean autoAck = true;
//        channel.basicQos(1);
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);

//        channel.basicConsume(QUEUE_NAME, autoAck, (s, delivery) -> {
//            System.out.println(s);
//            System.out.println(delivery);
//            System.out.println("deliverCallback");
//        }, consumerTag -> {
//            System.out.println("consumerTag");
//        });

    }

    private static void doWork(long time) throws InterruptedException {
        Thread.sleep(time);
    }

}
