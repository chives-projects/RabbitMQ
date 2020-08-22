package com.csc.rabbitmqdemo.template.receiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TransactionReceiver {

	@RabbitListener(queues = "transition")
	public void process(Message message, Channel channel) throws IOException {
		System.out.println("TransactionReceiver  : " + new String(message.getBody()));

	}

}
