package ru.ifmo.monolith.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.ifmo.monolith.producer.KafkaProducer;
import ru.ifmo.monolith.service.BookingService;

import static ru.ifmo.monolith.booking.BookingStatus.CONFIRMED;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuccessPaymentConsumer {

    private final BookingService bookingService;
    private final KafkaProducer kafkaProducer;

    @RabbitListener(queues = "${rabbitmq.successQueue}")
    public void handlePaymentSuccess(Integer bookingId) {
        try {
            bookingService.setStatus(bookingId, CONFIRMED);
            var booking = bookingService.getById(bookingId);
            kafkaProducer.send(booking);
        } catch (RuntimeException ex) {
            log.error("Couldn't handle success payment for id: {}", bookingId, ex);
        }
    }
}
