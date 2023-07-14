package net.currencyservice.rest;

import java.math.BigDecimal;
import java.util.Currency;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.currencyservice.busines.CurrencyRateService;
import net.currencyservice.domain.dto.response.CurrencyConvertResponseDto;
import net.currencyservice.domain.dto.response.CurrencyRateResponseDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequestMapping("/api/rates")
@RequiredArgsConstructor
@RestController
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    @GetMapping
    public Mono<CurrencyRateResponseDto> getCurrencyRate(@RequestParam(required = false) Currency currency) {
        log.info("Request for GET currency rate by currency: {}", currency);
        return currencyRateService.getCurrencyRate(currency);
    }

    @GetMapping("/all")
    public Flux<CurrencyRateResponseDto> getCurrencyRates() {
        log.info("Request for GET all currency rates");
        return currencyRateService.getCurrencyRates();
    }

    @GetMapping("/convert")
    public Mono<CurrencyConvertResponseDto> convertCurrency(@RequestParam Currency sourceCurrency,
                                                            @RequestParam Currency targetCurrency,
                                                            @RequestParam @Valid @Positive BigDecimal amount
    ) {
        log.info("Request for GET convert currency, from: {}, to: {}, amount: {}", sourceCurrency, targetCurrency, amount);
        return currencyRateService.convertCurrency(sourceCurrency, targetCurrency, amount);
    }
}
