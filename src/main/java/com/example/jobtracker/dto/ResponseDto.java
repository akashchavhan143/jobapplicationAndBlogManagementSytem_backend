package com.example.jobtracker.dto;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class ResponseDto<T> {
    private String message;
    private T data;
    private boolean success;
    private HttpStatus status;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ResponseDto() {}

    public ResponseDto(String message, T data, boolean success, HttpStatus status) {
        this.message = message;
        this.data = data;
        this.success = success;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
