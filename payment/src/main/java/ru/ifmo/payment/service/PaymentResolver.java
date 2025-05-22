package ru.ifmo.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.internal.PaymentRequest;
import ru.ifmo.common.dto.internal.PaymentResponse;
import ru.ifmo.payment.cache.PaymentCache;
import ru.loolzaaa.youkassa.model.Payment;
import ru.loolzaaa.youkassa.pojo.Amount;
import ru.loolzaaa.youkassa.pojo.Confirmation;
import ru.loolzaaa.youkassa.pojo.Currency;
import ru.loolzaaa.youkassa.processors.PaymentProcessor;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentResolver {

    private final PaymentProcessor paymentProcessor;
    private final PaymentCache paymentCache;

    @Value("${yookassa.redirect-url}")
    private String returnUrl;

    @Value("${yookassa.payment-description}")
    private String description;

    public PaymentResponse resolve(PaymentRequest paymentRequest) {
        var amount = buildAmount(paymentRequest);
        var confirmation = buildConfirmation();
        var payment = buildPayment(amount, confirmation);
        var receivedPayment = paymentProcessor.create(payment, null);
        paymentCache.put(paymentRequest.getBookingId(), receivedPayment);
        return PaymentResponse.builder()
                .paymentLink(receivedPayment.getConfirmation().getConfirmationUrl())
                .bookingId(paymentRequest.getBookingId())
                .build();
    }

    private Payment buildPayment(Amount amount, Confirmation confirmation) {
        return Payment.builder()
                .amount(amount)
                .description(description)
                .confirmation(confirmation)
                .build();
    }

    private Confirmation buildConfirmation() {
        return Confirmation.builder()
                .type(Confirmation.Type.REDIRECT)
                .returnUrl(returnUrl)
                .build();
    }

    private Amount buildAmount(PaymentRequest paymentRequest) {
        return Amount.builder()
                .value(String.valueOf(paymentRequest.getAmount()))
                .currency(Currency.RUB)
                .build();
    }
}
