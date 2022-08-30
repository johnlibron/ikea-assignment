package com.ikea.warehouseapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class ApiError {

    private final String timestamp;

    private final int status;

    private final List<String> errors;

    private final String message;

    private final String path;

    public ApiError(HttpStatus status, List<String> errors, String message, String path) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(formatter);
        this.status = status.value();
        this.errors = errors;
        this.message = message;
        this.path = path;
    }
}
