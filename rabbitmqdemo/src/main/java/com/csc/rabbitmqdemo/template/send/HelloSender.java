package com.csc.rabbitmqdemo.template.send;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class HelloSender {

    @Autowired
//	private AmqpTemplate amqpTemplate;
    private RabbitTemplate rabbitTemplate;

    public void send(String msg) {
        rabbitTemplate.setMandatory(true);
        /**
         * 创建一个消息是否投递成功的回调方法
         */
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("消息发送成功");
            } else {
                System.out.println("publishConfirm消息发送到交换器被退回,id:" + correlationData.getId() + ",原因：" + cause);
            }
        }));
        /**
         * 创建一个消息是否被队列接收的监听对象，如果没有队列接收发送出的消息，则调用此方法进行后续处理
         */
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("通过交换器:" + exchange +
                    "和路由:" + routingKey + "发送消息未找到符合条件的队列，消息被退回，" +
                    "错误编码replyCode：" + replyCode +
                    ",错误描述：" + replyText +
                    ",被退回的消息是：" + new String(message.getBody()));
        }));
        /**
         * 扩展点，在消息转换完成之后，发送之前调用；可以修改消息属性、消息头信息
         */
        rabbitTemplate.setBeforePublishPostProcessors((message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setExpiration("10000");
            messageProperties.setPriority(9);
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        }));

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendMsg = msg + time.format(new Date()) + " hello ";

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setTimestamp(new Date());
        messageProperties.setMessageId("id");

        Message message = MessageBuilder.withBody(sendMsg.getBytes()).andProperties(messageProperties).build();
        Message returnMessage = MessageBuilder.withBody(sendMsg.getBytes()).build();

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(message.getMessageProperties().getMessageId());
        correlationData.setReturnedMessage(returnMessage);

//        rabbitTemplate.convertAndSend("directExchange","hesadfallo", message, correlationData);
//        rabbitTemplate.convertAndSend("exchange", "routingKey", message, (message1) -> {
//            MessageProperties msgProperties = message1.getMessageProperties();
//            msgProperties.setPriority(1);
//            msgProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//            return message1;
//        }, correlationData);


//        System.out.println("Sender1 : " + sendMsg);
        rabbitTemplate.convertAndSend("hello", sendMsg);
    }

}