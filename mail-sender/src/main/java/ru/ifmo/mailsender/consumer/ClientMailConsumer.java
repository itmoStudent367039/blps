package ru.ifmo.mailsender.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.booking.BookingModel;
import ru.ifmo.mailsender.service.SimpleEmailSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientMailConsumer {

    private final ObjectMapper objectMapper;
    private final SimpleEmailSender javaMailSender;

    @KafkaListener(
            topics = "${application.kafka.topic-name}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(byte[] message) {
        try {
            var payload = objectMapper.readValue(message, BookingModel.class);
            javaMailSender.sendMail(payload);
        } catch (Exception e) {
            log.error("Error while sending email", e);
        }
    }
}
