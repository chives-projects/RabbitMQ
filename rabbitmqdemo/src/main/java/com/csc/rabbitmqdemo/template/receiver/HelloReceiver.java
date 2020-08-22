package com.csc.rabbitmqdemo.template.receiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HelloReceiver {

    @RabbitListener(queues = "hello")
    public void process(Message message, Channel channel) throws IOException {
        try {
            System.out.println("Receiver  : " + new String(message.getBody()));
            int i = 1 / 0;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                System.out.println("消息已重复处理失败,拒绝再次接收...");
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println("消息即将再次返回队列处理...");
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }


//        System.out.println("Receiver  : " + new String(message.getBody()));
//        int i = 1 / 0;
        // true 发送给下一个消费者
        // false 谁都不接受，从队列中删除
        // 拒绝消息，RabbitMQ把消息发送给下一个监听hello的队列(HelloReceiver2或CheckReceiver)
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
//		channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }

}
