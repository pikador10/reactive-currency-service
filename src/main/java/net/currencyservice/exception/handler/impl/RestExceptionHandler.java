package net.currencyservice.exception.handler.impl;

import java.util.List;

import net.currencyservice.exception.CurrencyRateApiException;
import net.currencyservice.exception.CurrencyRateStatusApiException;
import net.currencyservice.exception.handler.ErrorBody;
import net.currencyservice.exception.handler.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler({CurrencyRateStatusApiException.class})
	public ResponseEntity<ErrorBody> handleCurrencyRateStatusApiException(CurrencyRateStatusApiException ex) {
		var errorMessages = List.of(new ErrorMessage(ex.getClass().getSimpleName(), ex.responseMessage(), ex.responseHttpStatus().getReasonPhrase()));
		return new ResponseEntity<>(new ErrorBody(errorMessages), ex.responseHttpStatus());
	}

	@ExceptionHandler({CurrencyRateApiException.class})
	public ResponseEntity<ErrorBody> handleCurrencyRateApiException(CurrencyRateApiException ex) {
		var errorMessages = List.of(new ErrorMessage(ex.getClass().getSimpleName(), ex.responseMessage(), ex.responseHttpStatus().getReasonPhrase()));
		return new ResponseEntity<>(new ErrorBody(errorMessages), ex.responseHttpStatus());
	}
}
