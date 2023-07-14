package net.currencyservice.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;

public class CurrencyRateStatusApiException extends AbstractRestAPIException {
    public CurrencyRateStatusApiException(String message) {
        super(message);
    }
    @Override
    public HttpStatus responseHttpStatus() {
        return INTERNAL_SERVER_ERROR;
    }

    @Override
    public String responseMessage() {
        return getMessage();
    }
}
