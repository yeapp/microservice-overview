package com.patapchuk.song.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class SongServiceApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = SongNotFoundException.class)
    public ResponseEntity<Object> handleSongServiceNotFoundException(SongNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(getSongServiceApiException(e.getMessage(), status), status);
    }

    @ExceptionHandler(value = {SongRequestException.class})
    public ResponseEntity<Object> handleSongServiceRequestException(SongRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(getSongServiceApiException(e.getMessage(), status), status);
    }

    private SongServiceApiException getSongServiceApiException(String message, HttpStatus status) {
        return new SongServiceApiException(
                status,
                status.toString(),
                message,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }
}
