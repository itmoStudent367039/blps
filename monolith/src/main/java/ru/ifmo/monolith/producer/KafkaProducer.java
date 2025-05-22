package ru.ifmo.monolith.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.ifmo.common.dto.booking.BookingModel;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> template;

    @Value("${application.topic-name}")
    private String topic;

    private final ObjectMapper objectMapper;

    public void send(BookingModel booking) {
        try {
            var payload = objectMapper.writeValueAsBytes(booking);
            template
                    .send(topic, payload)
                    .whenComplete(this::kafkaProducerCallback);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize booking: {}", booking, e);
        } catch (RuntimeException ex) {
            log.error("Sending failed", ex);
        }
    }

    private void kafkaProducerCallback(SendResult<String, byte[]> result, Throwable ex) {
        if (ex != null) {
            log.error("Kafka send failed: {}", ex.getMessage(), ex);
        } else {
            log.info("Kafka message sent successfully: topic={}, offset={}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().offset());
        }
    }
}
