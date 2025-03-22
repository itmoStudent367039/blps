package ru.ifmo.payment.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.common.dto.PaymentRequest;
import ru.ifmo.common.dto.PaymentResponse;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @PostMapping
    public ResponseEntity<PaymentResponse> getPaymentLink(@RequestBody PaymentRequest paymentRequest) {
        var paymentDto = PaymentResponse.builder()
                .paymentLink("/stub/payment" + paymentRequest.getAmount())
                .build();
        return ok(paymentDto);
    }
}
