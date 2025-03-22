package ru.ifmo.payment.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.payment.dto.PaymentHookDto;
import ru.ifmo.payment.service.PaymentHookService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/payment-hook")
@RequiredArgsConstructor
public class PaymentHookController {

    private final PaymentHookService paymentHookService;

    @PostMapping
    public ResponseEntity<Void> hookFromPayment(@RequestBody PaymentHookDto paymentDto) {
        paymentHookService.handleHook(paymentDto);
        return ok().build();
    }
}
