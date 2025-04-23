package ru.ifmo.monolith.config.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("spring.datasource")
@Configuration
@Data
public class BookingProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
