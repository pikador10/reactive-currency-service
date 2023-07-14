package net.currencyservice.busines;

import static org.springframework.web.util.UriComponentsBuilder.*;

import java.util.Currency;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.websocket.DecodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.currencyservice.domain.dto.external.CurrencyRateExternalDto;
import net.currencyservice.exception.handler.CurrencyRateExceptionHandler;
import net.currencyservice.property.CurrencyRateProperty;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class CurrencyRateClient {

    private final WebClient webClient;
    private final CurrencyRateProperty currencyRateProperty;
    private final CurrencyRateExceptionHandler currencyRateExceptionHandler;

    public Mono<CurrencyRateExternalDto> getCurrencyRate(Currency currency) {
        log.debug("Started getting currency rate info from external api, by currency: {}", currency);

        return webClient.get()
            .uri(buildHttpUrl(currency.getCurrencyCode()))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, currencyRateExceptionHandler::handleStatusErrorResponse)
            .bodyToMono(CurrencyRateExternalDto.class)
            .onErrorMap(InvalidFormatException.class, currencyRateExceptionHandler::handleInvalidFormatException)
            .onErrorMap(WebClientResponseException.class, currencyRateExceptionHandler::handleWebClientResponseException)
            .onErrorMap(WebClientRequestException.class, currencyRateExceptionHandler::handleWebClientRequestException)
            .onErrorMap(DecodeException.class, currencyRateExceptionHandler::handleDecodeException);
    }

    private String buildHttpUrl(String currency) {
        return fromHttpUrl(currencyRateProperty.getLatestUrl()).queryParam("base", currency).toUriString();
    }
}
