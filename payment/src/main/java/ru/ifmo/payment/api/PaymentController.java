package ru.ifmo.payment.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.common.dto.internal.PaymentRequest;
import ru.ifmo.common.dto.internal.PaymentResponse;
import ru.ifmo.payment.cache.PaymentCache;
import ru.ifmo.payment.service.PaymentResolver;
import ru.loolzaaa.youkassa.processors.WebhookProcessor;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentResolver paymentResolver;

    @PostMapping
    public ResponseEntity<PaymentResponse> getPaymentLink(@RequestBody PaymentRequest paymentRequest) {
        var paymentDto = paymentResolver.resolve(paymentRequest);
        return ok(paymentDto);
    }
}
