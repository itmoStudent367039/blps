package ru.ifmo.monolith.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MonolithException extends RuntimeException {

    private final HttpStatus status;

    public MonolithException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
