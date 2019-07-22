package com.freenow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParseValueException extends RuntimeException {

    static final long serialVersionUID = -3387516993334229948L;

    public ParseValueException(String message) {
        super(message);
    }
}
