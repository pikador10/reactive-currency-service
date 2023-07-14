package net.currencyservice.busines.impl;

import static net.currencyservice.util.CurrencyRateUtil.convert;

import java.math.BigDecimal;
import java.util.Currency;

import lombok.RequiredArgsConstructor;
import net.currencyservice.busines.CurrencyRateClient;
import net.currencyservice.busines.CurrencyRateService;
import net.currencyservice.domain.CurrencyRate;
import net.currencyservice.domain.dto.response.CurrencyConvertResponseDto;
import net.currencyservice.domain.dto.response.CurrencyRateResponseDto;
import net.currencyservice.mapper.CurrencyRateMapper;
import net.currencyservice.repository.CurrencyRateRepository;
import net.currencyservice.validation.CurrencyRateValidationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateRepository repository;
    private final CurrencyRateClient currencyRateClient;
    private final CurrencyRateMapper currencyRateMapper;
    private final CurrencyRateValidationService currencyRateValidationService;

    @Scheduled(cron = "${currency.external.update-rate-cron}")
    public void updateCurrencyRate() {
        repository.findAll()
            .flatMap(entity -> currencyRateClient.getCurrencyRate(entity.getCurrency())
                .map(response -> {
                    var currencyRate = currencyRateMapper.toCurrencyRate(response);
                    currencyRate.setId(entity.getId());
                    return currencyRate;
                })
                .flatMap(repository::save))
            .subscribe();
    }

    @Override
    public Flux<CurrencyRateResponseDto> getCurrencyRates() {
        return repository.findAll()
            .map(currencyRateMapper::toCurrencyRateResponseDto);
    }

    @Override
    public Mono<CurrencyRateResponseDto> getCurrencyRate(Currency currency) {
        currency = currencyRateValidationService.validateForBaseCurrency(currency);
        return fetchAndSaveCurrencyRateIfNotExists(currency)
            .map(currencyRateMapper::toCurrencyRateResponseDto);
    }

    @Override
    public Mono<CurrencyConvertResponseDto> convertCurrency(Currency sourceCurrency, Currency targetCurrency, BigDecimal amount) {
        return fetchAndSaveCurrencyRateIfNotExists(sourceCurrency)
            .map(entity -> {
                var targetRate = entity.getRates().get(targetCurrency.getCurrencyCode());
                return new CurrencyConvertResponseDto(targetRate, sourceCurrency, targetCurrency, convert(amount, BigDecimal.valueOf(targetRate)));
            });
    }

    private Mono<CurrencyRate> fetchAndSaveCurrencyRateIfNotExists(Currency sourceCurrency) {
        return repository.findByCurrency(sourceCurrency)
            .switchIfEmpty(Mono.defer(() -> currencyRateClient.getCurrencyRate(sourceCurrency)
                .map(currencyRateMapper::toCurrencyRate))
                .flatMap(repository::save));
    }
}
