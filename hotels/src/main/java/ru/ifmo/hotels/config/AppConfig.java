package ru.ifmo.hotels.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.common.validator.BookingDateValidator;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BookingDateValidator bookingDateValidator() {
        return new BookingDateValidator();
    }
}
