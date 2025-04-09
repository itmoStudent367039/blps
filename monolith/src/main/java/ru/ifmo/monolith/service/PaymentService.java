package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.common.dto.PaymentRequest;
import ru.ifmo.common.dto.PaymentResponse;
import ru.ifmo.monolith.exception.IntegrationException;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate;

    @Value("${payment.url}")
    private String paymentUrl;

    public PaymentResponse resolvePayment(PaymentRequest paymentRequest) {
        try {
            return restTemplate.postForObject(paymentUrl, paymentRequest, PaymentResponse.class);
        } catch (RestClientException e) {
            log.error("Error while trying to resolve payment", e);
            throw new IntegrationException("Payment service is unavailable", BAD_GATEWAY);
        }
    }
}
