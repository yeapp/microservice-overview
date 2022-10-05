package com.patapchuk.resource.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ResourceApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceServiceNotFoundException(ResourceNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(getResourceApiException(e.getMessage(), status), status);
    }

    @ExceptionHandler(value = ResourceInternalServerErrorException.class)
    public ResponseEntity<Object> handleResourceServiceInternalServerErrorException(ResourceInternalServerErrorException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(getResourceApiException(e.getMessage(), status), status);
    }


    @ExceptionHandler(value = {ResourceRequestException.class})
    public ResponseEntity<Object> handleResourceServiceRequestException(ResourceRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(getResourceApiException(e.getMessage(), status), status);
    }

    private ResourceApiException getResourceApiException(String message, HttpStatus status) {
        return new ResourceApiException(
                status,
                status.toString(),
                message,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }
}
