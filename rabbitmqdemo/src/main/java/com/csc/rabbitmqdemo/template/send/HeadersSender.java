
package com.csc.rabbitmqdemo.template.send;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

@Component
public class HeadersSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send() {
		Map<String, Object> headers = new Hashtable<String, Object>();
		headers.put("name", "jack");
		headers.put("age", 30);
		String content = headers.toString();
		MessageProperties props = MessagePropertiesBuilder.newInstance()
				.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).setMessageId("123").setHeader("age", "30")
				.build();
		Message message = MessageBuilder.withBody(content.getBytes()).andProperties(props).build();

		System.out.println("sender1 : " + headers.toString());
		rabbitTemplate.convertAndSend("headersExchange", "", message);

	}

}
