package com.csc.rabbitmqdemo.template.send;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TransactionSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Transactional(rollbackFor = Exception.class)
	public void send(String msg) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sendMsg = msg + time.format(new Date()) + " This is a transaction message！ ";
		/**
		 * 这里可以执行数据库操作
		 * 
		 **/
		System.out.println("TransactionSender : " + sendMsg);

		rabbitTemplate.convertAndSend("transition", sendMsg);

	}

}
