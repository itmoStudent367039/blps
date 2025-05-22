package ru.ifmo.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import ru.loolzaaa.youkassa.client.ApiClient;
import ru.loolzaaa.youkassa.client.ApiClientBuilder;
import ru.loolzaaa.youkassa.model.Webhook;
import ru.loolzaaa.youkassa.processors.PaymentProcessor;
import ru.loolzaaa.youkassa.processors.WebhookProcessor;

@Configuration
public class YookassaConfig {

    @Value("${yookassa.webhook-url}")
    private String applicationUrl;

    @Value("${yookassa.auth.key}")
    private String key;

    @Value("${yookassa.auth.account-id}")
    private String accountId;

    @Bean
    public ApiClient apiClient() {
        return ApiClientBuilder.newBuilder()
                .configureBasicAuth(
                        accountId,
                        key
                )
                .build();
    }

    @Bean
    public WebhookProcessor webhookProcessor(ApiClient apiClient) {
        return new WebhookProcessor(apiClient);
    }

    @Bean
    public PaymentProcessor paymentProcessor(ApiClient apiClient) {
        return new PaymentProcessor(apiClient);
    }

    @EventListener
    public void setUpWebhook(WebhookProcessor webhookProcessor) {
        webhookProcessor.create(Webhook.builder()
                .event("payment.succeeded")
                .url(applicationUrl)
                .build(), null);
        webhookProcessor.create(Webhook.builder()
                .event("payment.waiting_for_capture")
                .url(applicationUrl)
                .build(), null);
        webhookProcessor.create(Webhook.builder()
                .event("payment.canceled")
                .url(applicationUrl)
                .build(), null);
    }
}
