
package com.csc.rabbitmqdemo.template.send;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FanoutSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        String msgString="fanoutSender : hello i am anumbrella";
        System.out.println(msgString);
        rabbitTemplate.convertAndSend("fanoutExchange","sfasdf", msgString);
    }

}