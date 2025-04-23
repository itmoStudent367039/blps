package ru.ifmo.payment.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.common.dto.internal.PaymentRequest;
import ru.ifmo.common.dto.internal.PaymentResponse;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @PostMapping
    public ResponseEntity<PaymentResponse> getPaymentLink(@RequestBody PaymentRequest paymentRequest) {
        var paymentDto = PaymentResponse.builder()
                .paymentLink("/stub/payment/" + paymentRequest.getAmount())
                .bookingId(paymentRequest.getBookingId())
                .build();
        return ok(paymentDto);
    }
}
