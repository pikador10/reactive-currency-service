package net.currencyservice.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractRestAPIException extends RuntimeException {

    protected AbstractRestAPIException(String message) {
        super(message);
    }

    protected AbstractRestAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract HttpStatus responseHttpStatus();

    public abstract String responseMessage();
}