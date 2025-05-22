package ru.ifmo.hotels.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HotelsException extends RuntimeException {

    private final HttpStatus status;

    public HotelsException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
