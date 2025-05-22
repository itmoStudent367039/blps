package ru.ifmo.monolith.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.successQueue}")
    private String successQueue;

    @Value("${rabbitmq.failureQueue}")
    private String failureQueue;

    @Value("${rabbitmq.successRoutingKey}")
    private String successRoutingKey;

    @Value("${rabbitmq.failureRoutingKey}")
    private String failureRoutingKey;

    @Bean
    public Declarables rabbitMqBindings() {
        Queue successPaymentQueue = new Queue(successQueue, true);
        Queue failurePaymentQueue = new Queue(failureQueue, true);
        TopicExchange topicExchange = new TopicExchange(exchange);

        return new Declarables(
                successPaymentQueue,
                failurePaymentQueue,
                topicExchange,
                BindingBuilder.bind(successPaymentQueue).to(topicExchange).with(successRoutingKey),
                BindingBuilder.bind(failurePaymentQueue).to(topicExchange).with(failureRoutingKey)
        );
    }
}