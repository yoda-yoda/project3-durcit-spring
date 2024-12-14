package org.durcit.be.system.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    public Queue postNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 60000); // 60,000ms = 1분
        return new Queue("postNotificationQueue", true, false, false, args);
    }

    @Bean
    public Binding postNotificationBinding(DirectExchange postExchange, Queue postNotificationQueue) {
        return BindingBuilder.bind(postNotificationQueue).to(postExchange).with("post.notify");
    }
}
