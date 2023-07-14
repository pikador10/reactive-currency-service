package net.currencyservice.exception.handler.impl;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.websocket.DecodeException;
import lombok.extern.slf4j.Slf4j;
import net.currencyservice.exception.CurrencyRateApiException;
import net.currencyservice.exception.CurrencyRateStatusApiException;
import net.currencyservice.exception.handler.CurrencyRateExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CurrencyRateExceptionHandlerImpl implements CurrencyRateExceptionHandler {

    @Override
    public Mono<? extends Throwable> handleStatusErrorResponse(ClientResponse response) {
        log.error("External API returned non-successful status code: {}", response.statusCode());
        return Mono.error(new CurrencyRateStatusApiException("Failed to retrieve currency rate from external API"));
    }

    @Override
    public RuntimeException handleWebClientResponseException(WebClientResponseException ex) {
        log.error("Error response received from external API with status code: {}, message: {}", ex.getStatusCode(), ex.getMessage());
        return new CurrencyRateApiException("Error occurred while retrieving currency rate from external API", ex.getCause());
    }

    @Override
    public Throwable handleWebClientRequestException(WebClientRequestException ex) {
        log.error("Error occurred while making a request to the external API: {}", ex.getMessage());
        return new CurrencyRateApiException("Error occurred while making a request to the external API", ex.getCause());
    }

    @Override
    public Throwable handleDecodeException(DecodeException ex) {
        log.error("Error occurred while decoding the response from the external API: {}", ex.getMessage());
        return new CurrencyRateApiException("Error occurred while decoding the response from the external API", ex.getCause());
    }

    @Override
    public Throwable handleInvalidFormatException(InvalidFormatException ex) {
        log.error("Invalid format exception occurred while deserializing response: {}", ex.getMessage());
        return new CurrencyRateApiException("Invalid currency format encountered", ex);
    }
}
