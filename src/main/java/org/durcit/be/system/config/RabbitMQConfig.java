package org.durcit.be.system.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange postExchange() {
        return new DirectExchange("postExchange", true, false);
    }

    @Bean
    public DirectExchange notifyExchange() {
        return new DirectExchange("notifyExchange", true, false);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue postNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 60000); // 60,000ms = 1분
        return new Queue("postNotificationQueue", true, false, false, args);
    }

    @Bean
    public Queue notificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 60000); // 1분 TTL
        return new Queue("notificationQueue", true, false, false, args);
    }

    @Bean
    public Binding postNotificationBinding(DirectExchange postExchange, Queue postNotificationQueue) {
        return BindingBuilder.bind(postNotificationQueue).to(postExchange).with("post.notify");
    }

    @Bean
    public Binding notificationBinding(DirectExchange notifyExchange, Queue notificationQueue) {
        return BindingBuilder.bind(notificationQueue).to(notifyExchange).with("notify.notify");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("postNotificationQueue");
        container.setAutoStartup(false);
        return container;
    }
}
