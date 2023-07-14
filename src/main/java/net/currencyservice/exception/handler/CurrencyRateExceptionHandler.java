package net.currencyservice.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.websocket.DecodeException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public interface CurrencyRateExceptionHandler {

    Mono<? extends Throwable> handleStatusErrorResponse(ClientResponse response);

    Throwable handleWebClientResponseException(WebClientResponseException ex);

    Throwable handleWebClientRequestException(WebClientRequestException ex);

    Throwable handleDecodeException(DecodeException ex);

    Throwable handleInvalidFormatException(InvalidFormatException ex);
}
