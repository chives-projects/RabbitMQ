package com.csc.rabbitmqdemo.template.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @PackageName: com.csc.rabbitmqdemo.template.config
 * @Author: csc
 * @Create: 2020-08-21 11:29
 * @Version: 1.0
 */
@Configuration
public class DefaultExchangeConfig {
    // ===============以下是验证rabbitmq默认 Exchange的队列和交互机==========

    /**
     * 申明hello队列
     *
     * @return
     */
    @Bean
    public Queue helloQueue() {
        return new Queue("hello");
    }

    /**
     * 申明user队列
     *
     * @return
     */
    @Bean
    public Queue userQueue() {
        return new Queue("user");
    }

    /**
     * 申明distribu队列
     *
     * @return
     */
    @Bean
    public Queue DistribuQueue() {
        return new Queue("distribu");
    }

    // ===============以上是验证rabbitmq默认 Exchange的队列和交互机==========
}
