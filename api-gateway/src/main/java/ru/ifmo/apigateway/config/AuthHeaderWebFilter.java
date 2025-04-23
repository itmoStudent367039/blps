package ru.ifmo.apigateway.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@Component
@Order(-1)
public class AuthHeaderWebFilter implements WebFilter {

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    if (auth == null || !auth.isAuthenticated()) {
                        return exchange;
                    }

                    String username = auth.getName();
                    String roles = auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));

                    var mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-User-Name", username)
                            .header("X-User-Roles", roles)
                            .build();

                    return exchange.mutate().request(mutatedRequest).build();
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }
}