package com.csc.rabbitmqdemo.template.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @PackageName: com.csc.rabbitmqdemo.template.config
 * @Author: csc
 * @Create: 2020-08-21 11:18
 * @Version: 1.0
 */
@Configuration
public class HeadersConfig {
    // ===============以下是验证Headers Exchange的队列和交互机==========
    @Bean
    public Queue headersQueueA() {
        return new Queue("headers.A");
    }

    @Bean
    HeadersExchange headersExchange() {
        return new HeadersExchange("headersExchange");
    }

    @Bean
    Binding bindingHeadersExchangeA(Queue headersQueueA, HeadersExchange headersExchange) {
        // 这里x-match有两种类型
        // all:表示所有的键值对都匹配才能接受到消息
        // any:表示只要有键值对匹配就能接受到消息
        return BindingBuilder.bind(headersQueueA).to(headersExchange).where("age").exists();
    }

    // ===============以上是验证Headers Exchange的队列和交互机==========
}
