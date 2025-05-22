package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.common.dto.internal.PaymentRequest;
import ru.ifmo.common.dto.internal.PaymentResponse;
import ru.ifmo.monolith.exception.IntegrationException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate;

    @Value("${payment.url}")
    private String paymentUrl;

    @Value("${payment.username}")
    private String username;

    @Value("${payment.credential}")
    private String credential;

    public PaymentResponse resolvePayment(PaymentRequest paymentRequest) {
        try {
            var entity = buildPaymentRequestBody(paymentRequest);
            return restTemplate.exchange(paymentUrl, POST, entity, PaymentResponse.class).getBody();
        } catch (RestClientException e) {
            log.error("Error while trying to resolve payment", e);
            throw new IntegrationException("Payment service is unavailable", BAD_GATEWAY);
        }
    }

    private HttpEntity<PaymentRequest> buildPaymentRequestBody(PaymentRequest paymentRequest) {
        var headers = new HttpHeaders();
        String auth = username + ":" + credential;
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", encodedAuth);
        return new HttpEntity<>(paymentRequest, headers);
    }
}
