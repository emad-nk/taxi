package com.freenow.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseBody
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> jsonParseException(InvalidFormatException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Malformed JSON"));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.error);
    }

    private class ApiError {
        // Either need to be public and final or have getters for ResponseEntity to be able to read the values
        public final ZonedDateTime timestamp;
        public final int status;
        public final HttpStatus error;
        public final String message;

        ApiError(HttpStatus status, String message) {
            this.timestamp = ZonedDateTime.now();
            this.status = status.value();
            this.error = status;
            this.message = message;
        }

    }
}
