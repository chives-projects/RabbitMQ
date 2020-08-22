package com.csc.rabbitmqdemo.template.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @PackageName: com.csc.rabbitmqdemo.template.config
 * @Author: csc
 * @Create: 2020-08-21 11:16
 * @Version: 1.0
 */
@Configuration
public class DirectConfig {
    // ===============以下是验证Direct Exchange的队列和交互机==========
    @Bean
    public Queue directQueueA() {
        return new Queue("direct.A");
    }

    @Bean
    public Queue directQueueB() {
        return new Queue("direct.B");
    }

    @Bean
    public Queue directQueueC() {
        return new Queue("direct.C");
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    @Bean
    Binding bindingDirectExchangeA(Queue directQueueA, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueA).to(directExchange).with("direct.a");
    }

    @Bean
    Binding bindingDirectExchangeB(Queue directQueueB, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueB).to(directExchange).with("direct.b");
    }

    @Bean
    Binding bindingDirectExchangeC(Queue directQueueC, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueC).to(directExchange).with("direct.c");
    }

    // ===============以上是验证Direct Exchange的队列和交互机==========
}
