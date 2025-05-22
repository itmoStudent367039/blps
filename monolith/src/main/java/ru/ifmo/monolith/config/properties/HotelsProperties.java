package ru.ifmo.monolith.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("hotels-datasource")
@Configuration
@Data
public class HotelsProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
