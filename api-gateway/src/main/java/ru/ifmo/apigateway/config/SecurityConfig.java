package ru.ifmo.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CorsSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static ru.ifmo.apigateway.config.RoleConstant.ROLE_ADMIN;
import static ru.ifmo.apigateway.config.RoleConstant.ROLE_SERVER;
import static ru.ifmo.apigateway.config.RoleConstant.ROLE_USER;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         PasswordEncoder encoder,
                                                         CustomUserDetailsService userDetailsService) {
        var manager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        manager.setPasswordEncoder(encoder);
        http.authenticationManager(manager);
        return http.authorizeExchange(exchange -> exchange
                        .pathMatchers(GET, "/monolith/api/booking/check-status?id=**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                        .pathMatchers(POST, "/monolith/api/booking").hasRole(ROLE_USER)
                        .pathMatchers(GET, "/monolith/api/destinations/**").permitAll()
                        .pathMatchers(GET, "/monolith/api/favorites").permitAll()
                        .pathMatchers(POST, "/monolith/api/hotels?page=**&size=**").permitAll()
                        .pathMatchers(POST, "/monolith/api/numbers?page=**&size=**").permitAll()
                        .pathMatchers(POST, "/payment/api").hasRole(ROLE_SERVER)
                        .pathMatchers()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(CsrfSpec::disable)
                .cors(CorsSpec::disable)
                .build();
    }
}
