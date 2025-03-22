package ru.ifmo.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.payment.dto.PaymentHookDto;
import ru.ifmo.payment.publisher.PaymentEventPublisher;

@Service
@RequiredArgsConstructor
public class PaymentHookService {

    private final PaymentEventPublisher eventPublisher;

    public void handleHook(PaymentHookDto paymentDto) {
        if (paymentDto != null && paymentDto.getSuccess() != null) {
            if (paymentDto.getSuccess()) {
                eventPublisher.publishPaymentSuccess(paymentDto.getBookingId());
            } else {
                eventPublisher.publishPaymentFailure(paymentDto.getBookingId());
            }
        }
    }
}
