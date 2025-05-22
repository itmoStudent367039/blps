package ru.ifmo.payment.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ifmo.payment.publisher.PaymentEventPublisher;
import ru.loolzaaa.youkassa.model.Payment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCache {

    private final Map<String, Integer> paymentIdToBookingId = new ConcurrentHashMap<>();

    private final PaymentEventPublisher paymentEventPublisher;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

    public void put(Integer bookingId, Payment receivedPayment) {
        paymentIdToBookingId.put(receivedPayment.getId(), bookingId);
        scheduler.schedule(
                () -> publishFailedIfContains(bookingId, receivedPayment.getId()),
                2,
                TimeUnit.MINUTES
        );
    }

    public Integer get(String paymentId) {
        return paymentIdToBookingId.get(paymentId);
    }

    public void remove(String paymentId) {
        paymentIdToBookingId.remove(paymentId);
    }

    private void publishFailedIfContains(Integer bookingId, String paymentId) {
        if (paymentIdToBookingId.containsKey(paymentId)) {
            log.warn("Payment with booking id: {} was expired", bookingId);
            paymentEventPublisher.publishPaymentFailure(bookingId);
            paymentIdToBookingId.remove(paymentId);
        }
    }
}
