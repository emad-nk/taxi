package com.freenow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConstraintsViolationException extends RuntimeException {

    static final long serialVersionUID = -3387516993224229948L;

    public ConstraintsViolationException(String message) {
        super(message);
    }

}
