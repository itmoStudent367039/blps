package ru.ifmo.payment.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.successRoutingKey}")
    private String successRoutingKey;

    @Value("${rabbitmq.failureRoutingKey}")
    private String failureRoutingKey;

    public void publishPaymentSuccess(Integer bookingId) {
        rabbitTemplate.convertAndSend(exchange, successRoutingKey, bookingId);
    }

    public void publishPaymentFailure(Integer bookingId) {
        rabbitTemplate.convertAndSend(exchange, failureRoutingKey, bookingId);
    }
}