package net.currencyservice.busines;

import java.math.BigDecimal;
import java.util.Currency;

import net.currencyservice.domain.dto.response.CurrencyConvertResponseDto;
import net.currencyservice.domain.dto.response.CurrencyRateResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CurrencyRateService {

    Flux<CurrencyRateResponseDto> getCurrencyRates();

    Mono<CurrencyRateResponseDto> getCurrencyRate(Currency currency);

    Mono<CurrencyConvertResponseDto> convertCurrency(Currency sourceCurrency, Currency targetCurrency, BigDecimal amount);
}
