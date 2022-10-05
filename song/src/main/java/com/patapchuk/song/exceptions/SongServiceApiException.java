package com.patapchuk.song.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class SongServiceApiException {
    private final HttpStatus status;
    private final String error;
    private final String message;
    private final ZonedDateTime timestamp;

    public SongServiceApiException(HttpStatus status, String error, String message, ZonedDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "SongServiceApiException{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
