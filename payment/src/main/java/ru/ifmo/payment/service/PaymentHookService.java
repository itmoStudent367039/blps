package ru.ifmo.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ifmo.payment.cache.PaymentCache;
import ru.ifmo.payment.dto.PaymentHookDto;
import ru.ifmo.payment.publisher.PaymentEventPublisher;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentHookService {

    private final PaymentEventPublisher eventPublisher;
    private final PaymentCache paymentCache;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean handleHook(PaymentHookDto paymentDto) {
        try {
            if (paymentDto != null && paymentDto.getEvent() != null) {
                String paymentId = resolvePaymentId(paymentDto);
                Integer bookingId = paymentCache.get(paymentId);
                if (bookingId == null) {
                    log.warn("Payment was expired with payment id: {}; Return 500 to Yookassa", paymentId);
                    return false;
                }
                if (paymentDto.getEvent().equals("payment.waiting_for_capture")) {
                    eventPublisher.publishPaymentSuccess(bookingId);
                    paymentCache.remove(paymentId);
                    log.info("Successfully published payment for booking with id: {}; returning 200", paymentId);
                } else if (paymentDto.getEvent().equals("payment.canceled")) {
                    eventPublisher.publishPaymentFailure(bookingId);
                    paymentCache.remove(paymentId);
                    log.info("Payment cancelled for booking with id: {}; returning 200", paymentId);
                } else {
                    log.warn("Unhandled payment event: {}", paymentDto.getEvent());
                    return false;
                }
                return true;
            }
            return false;
        } catch (RuntimeException ex) {
            log.error("Error handling payment hook", ex);
            return false;
        }
    }

    private String resolvePaymentId(PaymentHookDto paymentDto) {
        JsonNode objectNode = objectMapper.convertValue(paymentDto.getObject(), JsonNode.class);
        return objectNode.path("id").asText();
    }
}
