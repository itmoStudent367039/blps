package ru.ifmo.apigateway.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.ifmo.apigateway.RegistrationService;

@RestController
@RequestMapping("/registration/api")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public Mono<ResponseEntity<RegistrationResponse>> register(@RequestBody RegistrationRequest request) {
        return Mono.fromCallable(() -> registrationService.process(request));
    }
}
