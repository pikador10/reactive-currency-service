package net.currencyservice.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class CurrencyRateApiException extends AbstractRestAPIException {
    public CurrencyRateApiException(String message, Throwable cause) {
        super(message, cause);
    }
    @Override
    public HttpStatus responseHttpStatus() {
        return BAD_GATEWAY;
    }

    @Override
    public String responseMessage() {
        return getMessage();
    }
}
