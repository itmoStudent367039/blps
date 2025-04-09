package ru.ifmo.apigateway;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.apigateway.api.RegistrationRequest;
import ru.ifmo.apigateway.api.RegistrationResponse;
import ru.ifmo.apigateway.entity.Role;
import ru.ifmo.apigateway.entity.User;
import ru.ifmo.apigateway.repository.RoleRepository;
import ru.ifmo.apigateway.repository.UserRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;
import static ru.ifmo.apigateway.config.RoleConstant.ROLE_USER;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<RegistrationResponse> process(RegistrationRequest request) {
        if (userRepository.findByLogin(request.login()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegistrationResponse("User already exists"));
        }
        Role userRole = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        User user = buildEntity(request, userRole);
        userRepository.save(user);
        return status(CREATED).body(new RegistrationResponse("User registered successfully"));
    }

    private User buildEntity(RegistrationRequest request, Role userRole) {
        User user = new User();
        user.setLogin(request.login());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(userRole);
        return user;
    }
}
