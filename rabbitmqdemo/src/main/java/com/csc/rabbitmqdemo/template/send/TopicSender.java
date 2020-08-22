
package com.csc.rabbitmqdemo.template.send;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        String msg1 = "I am topic.mesaage msg======";
        System.out.println("sender1 : " + msg1);
        rabbitTemplate.convertAndSend("topicExchange", "topic.message", msg1);
        
        String msg2 = "I am topic.mesaages msg########";
        System.out.println("sender2 : " + msg2);
        rabbitTemplate.convertAndSend("topicExchange", "topic.messages", msg2);
    }

}
