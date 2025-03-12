package ru.ifmo.monolith.api.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ifmo.monolith.dto.ErrorResponse;
import ru.ifmo.monolith.exception.MonolithException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MonolithException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(MonolithException ex) {
        var response = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleCustomException(Exception ex) {
        var response = ErrorResponse.builder()
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError()
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(MethodArgumentNotValidException ex) {
        var message = new StringBuilder("Validation error: ");
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            message.append(String.format(
                    "Property '%s' - %s",
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            ));
            message.append("; ");
        });
        if (!message.isEmpty() && message.charAt(message.length() - 2) == ';') {
            message.setLength(message.length() - 2);
        }
        var response = ErrorResponse.builder()
                .message(message.toString())
                .build();
        log.error("Validation error: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(response);
    }
}
