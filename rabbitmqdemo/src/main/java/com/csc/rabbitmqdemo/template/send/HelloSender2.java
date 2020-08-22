package com.csc.rabbitmqdemo.template.send;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class HelloSender2 {

    @Autowired
//	private AmqpTemplate amqpTemplate;
    private RabbitTemplate rabbitTemplate;

    public void send(String msg) {
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("消息发送成功");
            } else {
                System.out.println("publishConfirm消息发送到交换器被退回,id:" + correlationData.getId() + ",原因：" + cause);
            }
        }));
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("通过交换器:" + exchange +
                    "和路由:" + routingKey + "发送消息未找到符合条件的队列，消息被退回，" +
                    "错误编码replyCode：" + replyCode +
                    ",错误描述：" + replyText +
                    ",被退回的消息是：" + new String(message.getBody()));
        }));

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendMsg = msg + time.format(new Date()) + " hello2 ";

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setTimestamp(new Date());
        messageProperties.setMessageId("id");

        Message message = MessageBuilder.withBody(sendMsg.getBytes()).andProperties(messageProperties).build();
        Message returnMessage = MessageBuilder.withBody(sendMsg.getBytes()).build();

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(message.getMessageProperties().getMessageId());
        correlationData.setReturnedMessage(returnMessage);

        rabbitTemplate.convertAndSend("hello", message, correlationData);
//        rabbitTemplate.convertAndSend("exchange", "routingKey", message, (message1) -> {
//            MessageProperties msgProperties = message1.getMessageProperties();
//            msgProperties.setPriority(1);
//            msgProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//            return message1;
//        }, correlationData);


//        System.out.println("Sender1 : " + sendMsg);
//        this.rabbitTemplate.convertAndSend("hello", sendMsg);
    }

}