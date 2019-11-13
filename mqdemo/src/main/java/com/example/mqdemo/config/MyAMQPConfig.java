package com.example.mqdemo.config;

/**
 * @Description
 * @Author shichao.chen
 * @Date 2019/11/12 10:11
 * @Version 1.0
 **/
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit//启动rabbit注解
public class MyAMQPConfig {
// 将消息以json格式传递
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}