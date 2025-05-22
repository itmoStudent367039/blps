package ru.ifmo.apigateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CorsSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.ifmo.apigateway.entity.Role;
import ru.ifmo.apigateway.entity.User;
import ru.ifmo.apigateway.repository.RoleRepository;
import ru.ifmo.apigateway.repository.UserRepository;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static ru.ifmo.common.security.RoleConstant.ROLE_ADMIN;
import static ru.ifmo.common.security.RoleConstant.ROLE_SERVER;
import static ru.ifmo.common.security.RoleConstant.ROLE_USER;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class GatewaySecurityConfig {

    @Value("${server-user.credential}")
    private String serverUserCredential;

    @Value("${server-user.name}")
    private String serverUserName;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ReactiveUserDetailsService userDetailsService) {
        var authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authManager.setPasswordEncoder(passwordEncoder());
        http.authenticationManager(authManager);
        http
                .csrf(CsrfSpec::disable)
                .cors(CorsSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(GET, "/monolith/api/booking/check-status**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                        .pathMatchers(POST, "/monolith/api/booking").hasRole(ROLE_USER)
                        .pathMatchers(POST, "/payment/api").hasRole(ROLE_SERVER)
                        .pathMatchers(GET, "/hotels/api/destinations/**").permitAll()
                        .pathMatchers(GET, "/hotels/api/favorites").permitAll()
                        .pathMatchers(GET, "/hotels/api/favorites-hotels").hasRole(ROLE_USER)
                        .pathMatchers(POST, "/hotels/api/hotels**").permitAll()
                        .pathMatchers(POST, "/hotels/api/tariffs/**").permitAll()
                        .pathMatchers(POST, "/hotels/api/numbers**").permitAll()
                        .pathMatchers(POST, "/registration/api").permitAll()
                        .anyExchange().denyAll()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeServerUser(UserRepository userRepository,
                                                                           PasswordEncoder passwordEncoder,
                                                                           RoleRepository roleRepository) {
        return event -> {
            if (userRepository.findByLogin(serverUserName).isPresent()) {
                log.info("Server user already exists.");
                return;
            }
            createServerUser(passwordEncoder, roleRepository, userRepository);
        };
    }

    private void createServerUser(PasswordEncoder passwordEncoder,
                                  RoleRepository roleRepository,
                                  UserRepository userRepository) {
        User server = new User();
        server.setPassword(passwordEncoder.encode(serverUserCredential));
        server.setLogin(serverUserName);
        Role serverRole = roleRepository.findByName("ROLE_" + ROLE_SERVER)
                .orElseThrow(() -> new RuntimeException("Role 'server' not found"));
        server.setRole(serverRole);
        userRepository.save(server);
    }
}