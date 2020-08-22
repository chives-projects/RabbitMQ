package com.csc.rabbitmqdemo.template.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String addresses;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses + ":" + port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        /** 如果要进行消息回调，则这里必须要设置为true */
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    /**
     * 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplateNew() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        // 使用Confirm模式，关闭掉事务
//        template.setChannelTransacted(true);
        return template;
    }

    /**
     * 声明transition队列，防止直接启动报错
     */
    @Bean
    public Queue transitionQueueA() {
        return new Queue("transition");
    }

    @Bean
    public Queue transitionQueue() {
        return new Queue("transition2");
    }

    /**
     * Spring Boot开启事务(异步模式)
     * 使用Confirm模式时，需要注释掉
     */
//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
////        container.setTransactionManager(rabbitTransactionManager());
////        container.setChannelTransacted(true);
//        // 开启手动确认
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
////        container.setQueues(transitionQueue());
////        container.setMessageListener(new TransitionConsumer());
//        return container;
//    }


    /**
     * 事务管理
     * @return
     */
    @Bean
    public RabbitTransactionManager rabbitTransactionManager() {
        return new RabbitTransactionManager(connectionFactory());
    }

    /**
     * 自定义消费者
     */
    public class TransitionConsumer implements ChannelAwareMessageListener {
        @Override
        public void onMessage(Message message, Channel channel) throws Exception {
            byte[] body = message.getBody();
            System.out.println("TransitionConsumer: " + new String(body));
            // 确认消息成功消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            // 除以0，模拟异常，进行事务回滚
            // int t = 1 / 0;
        }
    }

}
